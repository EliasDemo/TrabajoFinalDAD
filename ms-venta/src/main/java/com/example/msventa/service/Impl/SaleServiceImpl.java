package com.example.msventa.service.Impl;

import com.example.msventa.dto.*;
import com.example.msventa.entity.Sale;
import com.example.msventa.feign.ClientFeign;
import com.example.msventa.feign.OrderFeign;
import com.example.msventa.feign.ProductFeign;
import com.example.msventa.repository.SaleRepository;
import com.example.msventa.service.SaleService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private OrderFeign orderFeign;

    @Autowired
    private ClientFeign clientFeign;

    @Autowired
    private ProductFeign productFeign;

    @Override
    public List<Sale> listar() {

        List<Sale> sales = saleRepository.findAll();


        sales.forEach(sale -> {
            if (sale.getOrderId() != null) {
                try {

                    OrderDto orderDto = orderFeign.getById(sale.getOrderId()).getBody();
                    sale.setOrderDto(orderDto);


                    if (orderDto.getClientId() != null) {
                        ClientDto clientDto = clientFeign.findById(orderDto.getClientId()).getBody();
                        orderDto.setClientDto(clientDto);
                    }


                    orderDto.getOrderDetails().forEach(detail -> {
                        if (detail.getProductId() != null) {
                            ProductDto productDto = productFeign.getById(detail.getProductId()).getBody();
                            detail.setProductDto(productDto);  //
                        }
                    });
                } catch (Exception e) {
                    // Manejar errores al obtener los detalles
                    sale.setOrderDto(null);
                    System.out.println("Error al obtener detalles del pedido, cliente o productos para la venta con ID: " + sale.getId());
                }
            }
        });

        return sales;
    }

    @Override
    public Sale processSale(Integer orderId, String paymentMethod) {
        // Obtener los detalles del pedido desde ms-pedido
        OrderDto orderDto = orderFeign.getById(orderId).getBody();

        if (orderDto == null) {
            throw new RuntimeException("Pedido no encontrado para el ID: " + orderId);
        }

        // Calcular el monto total sumando los precios totales de los detalles del pedido
        Double totalAmount = orderDto.getOrderDetails().stream()
                .mapToDouble(OrderDetailDto::getTotalPrice) // Sumar todos los `totalPrice` de los detalles
                .sum();

        // Calcular los impuestos (por ejemplo, 18%)
        Double taxAmount = totalAmount * 0.18;
        Double totalWithTax = totalAmount + taxAmount;

        // Reducir el stock para cada producto en el microservicio ms-catalogo
        orderDto.getOrderDetails().forEach(detail -> {
            productFeign.reduceStock(detail.getProductId(), detail.getAmount());
        });

        // Crear y guardar la venta
        Sale sale = new Sale();
        sale.setOrderId(orderId);
        sale.setTotalAmount(totalWithTax);
        sale.setPaymentMethod(paymentMethod);
        sale.setStatus("paid");
        sale.setSaleDate(LocalDateTime.now());
        sale.setOrderDto(orderDto);

        return saleRepository.save(sale);
    }


    @Override
    public Sale getSaleById(Integer id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
    }

    @Override
    public List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public ReportDto generateReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Sale> sales = getSalesByDateRange(startDate, endDate);

        // Consolidar los datos en un DTO de reporte
        Double totalAmount = sales.stream().mapToDouble(Sale::getTotalAmount).sum();
        Integer totalSales = sales.size();

        ReportDto reportDto = new ReportDto();
        reportDto.setTotalAmount(totalAmount);
        reportDto.setTotalSales(totalSales);
        reportDto.setSales(sales);

        return reportDto;
    }
    @Override
    @Transactional
    public void cancelSale(Integer saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        if (!sale.getStatus().equalsIgnoreCase("paid")) {
            throw new RuntimeException("Only paid sales can be canceled");
        }

        OrderDto orderDto = orderFeign.getById(sale.getOrderId()).getBody();
        if (orderDto == null) {
            throw new RuntimeException("Order not found for the given Sale");
        }

        sale.setOrderDto(orderDto);

        // Devolver el stock de los productos
        sale.getOrderDto().getOrderDetails().forEach(orderDetail -> {
            try {
                productFeign.increaseStock(orderDetail.getProductId(), orderDetail.getAmount());
            } catch (FeignException.NotFound e) {
                // Manejo de producto no encontrado
                System.out.println("Warning: Product not found in catalog service for Product ID " + orderDetail.getProductId());
            } catch (Exception e) {
                throw new RuntimeException("Error updating product stock: " + e.getMessage());
            }
        });

        sale.setStatus("canceled");
        saleRepository.save(sale);
    }
}
