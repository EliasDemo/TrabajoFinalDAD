package com.example.msventa.service;

import com.example.msventa.dto.ReportDto;
import com.example.msventa.entity.Sale;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {
    public List<Sale> listar();
    public Sale processSale(Integer orderId, String paymentMethod);
    public Sale getSaleById(Integer id);

    public List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    public ReportDto generateReport(LocalDateTime startDate, LocalDateTime endDate);

    public void cancelSale(Integer saleId);
}
