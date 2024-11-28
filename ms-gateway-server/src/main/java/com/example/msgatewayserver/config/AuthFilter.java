package com.example.msgatewayserver.config;

import com.example.msgatewayserver.dto.TokenDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;


@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private WebClient.Builder webClient;


    public AuthFilter(WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient;
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            // Verifica si el encabezado Authorization está presente
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                return onError(exchange, HttpStatus.BAD_REQUEST);

            String tokenHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] chunks = tokenHeader.split(" ");

            // Verifica que el token sea un Bearer token
            if (chunks.length != 2 || !chunks[0].equals("Bearer"))
                return onError(exchange, HttpStatus.BAD_REQUEST);

            // Construir la URI correctamente usando UriComponentsBuilder
            String uri = UriComponentsBuilder.fromHttpUrl("http://ms-auth-service/auth/validate")
                    .queryParam("token", chunks[1])
                    .toUriString();

            // Llamada a ms-auth-service para validar el token
            return webClient.build()
                    .post()
                    .uri(uri)  // Utiliza la URI construida
                    .retrieve()
                    .bodyToMono(TokenDto.class)
                    .map(t -> {
                        // Si el token es válido, continúa con la cadena
                        return exchange;
                    })
                    .flatMap(chain::filter);
        });
    }

    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus status){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return ((ServerHttpResponse) response).setComplete();
    }


    public static class Config {}


}