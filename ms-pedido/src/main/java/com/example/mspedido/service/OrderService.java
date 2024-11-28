package com.example.mspedido.service;

import com.example.mspedido.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    public List<Order> list();
    public Optional<Order> findById(Integer id);
    public Order save(Order order);
    public Order update(Order order);
    public void delete(Integer id);
}
