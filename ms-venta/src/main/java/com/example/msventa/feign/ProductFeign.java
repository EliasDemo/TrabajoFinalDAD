package com.example.msventa.feign;

import com.example.msventa.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-catalogo-service", path = "/Product")
public interface ProductFeign {

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Integer id);

    @PutMapping("/{productId}/reduce-stock")
    ResponseEntity<Void> reduceStock(@PathVariable("productId") Integer productId, @RequestParam Integer amount);

    @PutMapping("/{id}/increase-stock")
    ResponseEntity<Void> increaseStock(@PathVariable("id") Integer id, @RequestParam("amount") Integer amount);
}
