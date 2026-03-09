package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Payment payment1;
    private Payment payment2;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        Map<String, String> data1 = new HashMap<>();
        data1.put("address", "Jl. A");
        data1.put("deliveryFee", "5000");
        payment1 = new Payment("p1", "order1", "Cash on Delivery", "PENDING", data1);
        Map<String, String> data2 = new HashMap<>();
        data2.put("voucherCode", "ESHOP1234ABC5678");
        payment2 = new Payment("p2", "order2", "Voucher", "SUCCESS", data2);
    }

    @Test
    void testSaveNewPayment() {
        paymentRepository.save(payment1);
        Optional<Payment> found = paymentRepository.findById("p1");
        assertTrue(found.isPresent());
        assertEquals("p1", found.get().getId());
    }

    @Test
    void testSaveUpdatePayment() {
        paymentRepository.save(payment1);
        payment1.setStatus("SUCCESS");
        paymentRepository.save(payment1);
        Optional<Payment> found = paymentRepository.findById("p1");
        assertTrue(found.isPresent());
        assertEquals("SUCCESS", found.get().getStatus());
    }

    @Test
    void testFindByIdValid() {
        paymentRepository.save(payment1);
        Optional<Payment> found = paymentRepository.findById("p1");
        assertTrue(found.isPresent());
    }

    @Test
    void testFindByIdInvalid() {
        Optional<Payment> found = paymentRepository.findById("nonexistent");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        List<Payment> all = paymentRepository.findAll();
        assertEquals(2, all.size());
    }
}