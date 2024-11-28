package com.example.msventa.controller;

import com.example.msventa.dto.ReportDto;
import com.example.msventa.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private SaleService saleService;

    @GetMapping("/sales")
    public ResponseEntity<ReportDto> getSalesReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        ReportDto report = saleService.generateReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }
}