package com.example.msventa.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Integer id;
    private String number;
    private Integer clientId;
    private ClientDto clientDto;
    private List<OrderDetailDto> orderDetails;
}
