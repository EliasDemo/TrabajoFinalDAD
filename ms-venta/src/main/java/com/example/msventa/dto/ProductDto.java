package com.example.msventa.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private String code;
    private Double price;
    private CategoryDto category;
}
