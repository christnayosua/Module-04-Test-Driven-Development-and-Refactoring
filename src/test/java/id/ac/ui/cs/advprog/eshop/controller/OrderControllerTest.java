package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.service.OrderService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void testCreateOrderPage() throws Exception {

        mockMvc.perform(get("/order/create"))
               .andExpect(status().isOk());

    }

    @Test
    void testOrderHistoryPage() throws Exception {

        mockMvc.perform(get("/order/history"))
               .andExpect(status().isOk());

    }

    @Test
    void testOrderPayPage() throws Exception {

        mockMvc.perform(get("/order/pay/123"))
               .andExpect(status().isOk());

    }

}