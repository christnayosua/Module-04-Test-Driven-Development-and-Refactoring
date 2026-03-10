package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testConstructorWithDefaultStatus() {
        Map<String, String> data = new HashMap<>();
        data.put("address", "Jl. Merdeka No. 1");
        data.put("deliveryFee", "10000");

        Payment payment = new Payment("id1", "order1", "Cash on Delivery", data);

        assertEquals("id1", payment.getId());
        assertEquals("order1", payment.getOrderId());
        assertEquals("Cash on Delivery", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals(data, payment.getPaymentData());
    }

    @Test
    void testConstructorWithCustomStatus() {
        Map<String, String> data = new HashMap<>();
        data.put("address", "Jl. Merdeka No. 1");

        Payment payment = new Payment("id2", "order2", "Bank Transfer", "SUCCESS", data);

        assertEquals("id2", payment.getId());
        assertEquals("order2", payment.getOrderId());
        assertEquals("Bank Transfer", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(data, payment.getPaymentData());
    }

    @Test
    void testSetterAndGetter() {
        Payment payment = new Payment("id3", "order3", "Voucher", new HashMap<>());

        payment.setStatus("REJECTED");
        payment.setMethod("Bank Transfer");

        assertEquals("REJECTED", payment.getStatus());
        assertEquals("Bank Transfer", payment.getMethod());
    }
}