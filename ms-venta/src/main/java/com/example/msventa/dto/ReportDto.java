package com.example.msventa.dto;

import com.example.msventa.entity.Sale;
import lombok.Data;

import java.util.List;

@Data
public class ReportDto {
    private Double totalAmount;
    private Integer totalSales;
    private List<Sale> sales;


}