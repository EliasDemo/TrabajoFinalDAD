package com.example.msventa.controller;

import com.example.msventa.dto.ErrorResponseDto;
import com.example.msventa.dto.OrderDto;
import com.example.msventa.entity.Sale;
import com.example.msventa.feign.OrderFeign;
import com.example.msventa.service.SaleService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/Sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private OrderFeign orderFeign;

    @GetMapping()
    public ResponseEntity<List<Sale>> list() {
        List<Sale> sales = saleService.listar();
        return ResponseEntity.ok(sales);
    }


    @PostMapping("/process/{orderId}")
    public ResponseEntity<?> processSale(@PathVariable Integer orderId, @RequestParam String paymentMethod) {
        try {
            // Verificar si el pedido existe usando Feign
            ResponseEntity<OrderDto> orderResponse = orderFeign.getById(orderId);

            // Verificar si la respuesta es 404 (pedido no encontrado)
            if (orderResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                String errorMessage = "Error: Pedido con ID " + orderId + " no encontrado.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(errorMessage));
            }

            if (!orderResponse.getStatusCode().is2xxSuccessful() || orderResponse.getBody() == null) {
                String errorMessage = "Error al obtener el pedido.";
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto(errorMessage));
            }

            // Procesar la venta si el pedido existe
            Sale sale = saleService.processSale(orderId, paymentMethod);
            return ResponseEntity.ok(sale);
        } catch (FeignException.NotFound e) {
            String errorMessage = "Error: Pedido con ID " + orderId + " no encontrado en el microservicio de pedidos.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(errorMessage));
        } catch (FeignException fe) {
            String errorMessage = "Error al comunicarse con el microservicio de pedidos: " + fe.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto(errorMessage));
        } catch (Exception e) {
            String errorMessage = "Error interno al procesar la venta: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto(errorMessage));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Integer id) {
        Sale sale = saleService.getSaleById(id);
        return ResponseEntity.ok(sale);
    }
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelSale(@PathVariable Integer id) {
        try {
            saleService.cancelSale(id);
            return ResponseEntity.ok().body("Sale canceled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }


}