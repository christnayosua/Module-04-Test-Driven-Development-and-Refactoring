package id.ac.ui.cs.advprog.eshop.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

        mockMvc.perform(get("/payment/admin/detail/123"))
               .andExpect(status().isOk());

    }

}