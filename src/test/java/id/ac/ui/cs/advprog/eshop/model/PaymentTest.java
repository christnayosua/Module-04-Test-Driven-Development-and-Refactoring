package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    @Test
    void testCreatePayment() {
        Map<String, String> data = new HashMap<>();
        data.put("address", "Jl. Merdeka No. 1");
        data.put("deliveryFee", "10000");
        Payment payment = new Payment("id1", "order1", "Cash on Delivery", "PENDING", data);
        assertEquals("id1", payment.getId());
        assertEquals("order1", payment.getOrderId());
        assertEquals("Cash on Delivery", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals(data, payment.getPaymentData());
    }
}