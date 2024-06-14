package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody List<Long> productIds) {
        Order order = orderService.createOrder(productIds);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<Product>> getProductsByOrderId(@PathVariable Long id) {
        List<Product> products = orderService.getProductsByOrderId(id);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<BigDecimal> getTotalAmountByOrderId(@PathVariable Long id) {
        BigDecimal totalAmount = orderService.getTotalAmountByOrderId(id);
        return ResponseEntity.ok(totalAmount);
    }
}
