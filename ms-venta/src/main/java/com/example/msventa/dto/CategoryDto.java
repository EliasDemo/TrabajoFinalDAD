package com.example.msventa.dto;

import lombok.Data;

@Data
public class CategoryDto {
    public Integer id;
    public String name;
    public String description;
    private String code;
}