package com.example.msventa.feign;

import com.example.msventa.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "ms-pedido-service", path = "/Order")
public interface OrderFeign {
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Integer id);
}