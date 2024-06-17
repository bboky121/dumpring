package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);

        BigDecimal totalAmount = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        Order order = new Order();
        order.setProducts(products);
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    @Cacheable(value = "orders", key = "#orderId")
    public List<Product> getProductsByOrderId(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return order.getProducts();
    }

    @Cacheable(value = "ordersTotals", key = "#orderID")
    public BigDecimal getTotalAmountByOrderId(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return order.getTotalAmount();
    }
}
