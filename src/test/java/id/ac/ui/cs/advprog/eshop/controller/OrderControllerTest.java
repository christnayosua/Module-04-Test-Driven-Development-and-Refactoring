package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private PaymentService paymentService;

    @Test
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/create"));
    }

    @Test
    void testCreateOrder() throws Exception {
        mockMvc.perform(post("/order/create")
                .param("author", "Irene"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));
    }

    @Test
    void testOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/history"));
    }

    @Test
    void testOrderHistory() throws Exception {
        List<Order> orders = new ArrayList<>();
        when(orderService.findAllByAuthor(anyString())).thenReturn(orders);

        mockMvc.perform(post("/order/history")
                .param("author", "Irene"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(view().name("order/history"));
    }

    @Test
    void testPayPage() throws Exception {
        mockMvc.perform(get("/order/pay/{orderId}", "123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orderId"))
                .andExpect(view().name("order/pay"));
    }
    
    @Test
    void testPayOrder() throws Exception {

        String orderId = "order123";
        String paymentId = "payment123";

        Product product = new Product();
        product.setProductId("p1");
        product.setProductName("Sample Product");
        product.setProductQuantity(1);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Order order = new Order(orderId, products, 200L, "Irene");

        Payment payment = new Payment(
                paymentId,
                orderId,
                "Bank Transfer",
                new HashMap<>()
        );

        when(orderService.findById(orderId)).thenReturn(order);
        when(paymentService.addPayment(any(Order.class), anyString(), any()))
                .thenReturn(payment);

        mockMvc.perform(post("/order/pay/{orderId}", orderId)
                .param("method", "Bank Transfer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/detail/" + paymentId));
    }
}