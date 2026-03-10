package id.ac.ui.cs.advprog.eshop.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentStatusTest {

    @Test
    void testGetValuePending() {
        PaymentStatus status = PaymentStatus.PENDING;
        assertEquals("PENDING", status.getValue());
    }

    @Test
    void testGetValueSuccess() {
        PaymentStatus status = PaymentStatus.SUCCESS;
        assertEquals("SUCCESS", status.getValue());
    }

    @Test
    void testGetValueRejected() {
        PaymentStatus status = PaymentStatus.REJECTED;
        assertEquals("REJECTED", status.getValue());
    }

    @Test
    void testContainsValidStatus() {
        assertTrue(PaymentStatus.contains("PENDING"));
        assertTrue(PaymentStatus.contains("SUCCESS"));
        assertTrue(PaymentStatus.contains("REJECTED"));
    }

    @Test
    void testContainsInvalidStatus() {
        assertFalse(PaymentStatus.contains("FAILED"));
        assertFalse(PaymentStatus.contains("PROCESSING"));
        assertFalse(PaymentStatus.contains(""));
    }
}