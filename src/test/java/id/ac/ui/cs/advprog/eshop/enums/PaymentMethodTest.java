package id.ac.ui.cs.advprog.eshop.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {

    @Test
    void testGetValueCashOnDelivery() {
        PaymentMethod method = PaymentMethod.CASH_ON_DELIVERY;
        assertEquals("Cash on Delivery", method.getValue());
    }

    @Test
    void testGetValueVoucher() {
        PaymentMethod method = PaymentMethod.VOUCHER;
        assertEquals("Voucher", method.getValue());
    }

    @Test
    void testGetValueBankTransfer() {
        PaymentMethod method = PaymentMethod.BANK_TRANSFER;
        assertEquals("Bank Transfer", method.getValue());
    }

    @Test
    void testContainsValidMethod() {
        assertTrue(PaymentMethod.contains("Cash on Delivery"));
        assertTrue(PaymentMethod.contains("Voucher"));
        assertTrue(PaymentMethod.contains("Bank Transfer"));
    }

    @Test
    void testContainsInvalidMethod() {
        assertFalse(PaymentMethod.contains("Credit Card"));
        assertFalse(PaymentMethod.contains("E-Wallet"));
        assertFalse(PaymentMethod.contains(""));
    }
}