package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private Product product1;
    private Product product2;
    private Order order;

    @BeforeEach
    public void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("product1");
        product1.setPrice(new BigDecimal("10"));

        product2 = new Product();
        product2.setId(2L);
        product2.setName("product2");
        product2.setPrice(new BigDecimal("20"));

        Order order = new Order();
        order.setId(1L);
        order.setProducts(Arrays.asList(product1, product2));
        order.setTotalAmount(new BigDecimal("30"));

        Mockito.when(orderService.createOrder(Mockito.anyList())).thenReturn(order);
        Mockito.when(orderService.getProductsByOrderId(1L)).thenReturn(Arrays.asList(product1, product2));
        Mockito.when(orderService.getTotalAmountByOrderId(1L)).thenReturn(new BigDecimal("30.00"));
    }

    @Test
    @WithMockUser(username = "bboky121", roles = {"USER"})
    public void TestCreateOrder() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, 2]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value("30"));
    }

    @Test
    @WithMockUser(username = "bboky121", roles = {"USER"})
    public void getProductsByOrderId() throws Exception {
        mockMvc.perform(get("/orders/1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("product1"))
                .andExpect(jsonPath("$[1].name").value("product2"));
    }

    @Test
    @WithMockUser(username = "bboky121", roles = {"USER"})
    public void getTotalAmountByOrderId() throws Exception {
        mockMvc.perform(get("/orders/1/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("30"));
    }

}
