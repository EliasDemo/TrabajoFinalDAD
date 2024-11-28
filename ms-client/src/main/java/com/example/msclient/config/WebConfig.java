package com.example.msclient.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar la carpeta externa para servir los archivos
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/BolsaLAboralImg/");
    }
}
