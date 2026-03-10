package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void testPaymentDetailPage() throws Exception {

        mockMvc.perform(get("/payment/detail"))
               .andExpect(status().isOk());

    }

    @Test
    void testPaymentAdminListPage() throws Exception {

        mockMvc.perform(get("/payment/admin/list"))
               .andExpect(status().isOk());

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
               .andExpect(status().isOk());

    }

}