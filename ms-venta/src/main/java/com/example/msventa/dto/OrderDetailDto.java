package com.example.msventa.dto;

import lombok.Data;

@Data
public class OrderDetailDto {
    private Integer productId;
    private ProductDto productDto;
    private Double price;
    private Integer amount;
    private Double totalPrice;
}
