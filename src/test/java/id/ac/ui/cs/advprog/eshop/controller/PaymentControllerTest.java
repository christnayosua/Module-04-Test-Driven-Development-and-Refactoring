package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void testPaymentFormPage() throws Exception {

        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/detail"));

    }

    @Test
    void testPaymentDetailPage() throws Exception {

        Payment payment = new Payment(
                "123",
                "order1",
                "Cash on Delivery",
                "PENDING",
                new HashMap<>()
        );

        when(paymentService.getPayment("123")).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("payment"))
                .andExpect(view().name("payment/detail"));

    }

    @Test
    void testPaymentAdminListPage() throws Exception {

        List<Payment> payments = new ArrayList<>();

        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("payments"))
                .andExpect(view().name("payment/list"));

    }

    @Test
    void testPaymentAdminDetailPage() throws Exception {

        Payment payment = new Payment(
                "123",
                "order1",
                "Cash on Delivery",
                "PENDING",
                new HashMap<>()
        );

        when(paymentService.getPayment("123")).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("payment"))
                .andExpect(view().name("payment/admin-detail"));

    }

    @Test
    void testSetStatus() throws Exception {

        Payment payment = new Payment(
                "123",
                "order1",
                "Cash on Delivery",
                "PENDING",
                new HashMap<>()
        );

        when(paymentService.getPayment("123")).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/123")
                .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));

    }
}