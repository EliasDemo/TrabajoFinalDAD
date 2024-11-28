package com.example.msventa.entity;

import com.example.msventa.dto.OrderDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Data
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer orderId;
    private Double totalAmount;
    private String status;
    private String paymentMethod;

    private LocalDateTime saleDate;

    @Transient
    private OrderDto orderDto;

}
