package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    public void setup() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("product1");
        product1.setPrice(new BigDecimal("10"));

        product2 = new Product();
        product2.setId(2L);
        product2.setName("product2");
        product2.setPrice(new BigDecimal("20"));

        Mockito.when(productRepository.findAllById(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(product1, product2));

        Order order = new Order();
        order.setId(1L);
        order.setProducts(Arrays.asList(product1, product2));
        order.setTotalAmount(new BigDecimal("30"));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
        Mockito.when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
    }

    @Test
    public void testCreateOrder() {
        Order order = orderService.createOrder(Arrays.asList(1L, 2L));
        assertThat(order.getTotalAmount()).isEqualTo(new BigDecimal("30"));
    }

    @Test
    public void testGetProductsByOrderId() {
        List<Product> products = orderService.getProductsByOrderId(1L);
        assertThat(products).contains(product1, product2);
    }

    @Test
    public void testGetTotalAmountByOrderId() {
        BigDecimal totalAmount = orderService.getTotalAmountByOrderId(1L);
        assertThat(totalAmount).isEqualTo(new BigDecimal("30"));
    }
}
