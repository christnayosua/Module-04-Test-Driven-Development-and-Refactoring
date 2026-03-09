package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private Map<String, String> validCodData;
    private Map<String, String> invalidCodData;

    @BeforeEach
    void setUp() {
        order = new Order("order1", new ArrayList<>(), 123L, "author");
        validCodData = new HashMap<>();
        validCodData.put("address", "Jl. Merdeka No. 1");
        validCodData.put("deliveryFee", "15000");

        invalidCodData = new HashMap<>();
        invalidCodData.put("address", "");
        invalidCodData.put("deliveryFee", "15000");
    }

    @Test
    void testAddPaymentCashOnDeliveryValid() {
        Payment result = paymentService.addPayment(order, "Cash on Delivery", validCodData);
        assertEquals("PENDING", result.getStatus());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCashOnDeliveryInvalidAddress() {
        Payment result = paymentService.addPayment(order, "Cash on Delivery", invalidCodData);
        assertEquals("REJECTED", result.getStatus());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCashOnDeliveryMissingFee() {
        Map<String, String> missingFee = new HashMap<>();
        missingFee.put("address", "Jl. A");
        // no deliveryFee
        Payment result = paymentService.addPayment(order, "Cash on Delivery", missingFee);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = new Payment("p1", order.getId(), "Cash on Delivery", "PENDING", validCodData);
        when(paymentRepository.findById("p1")).thenReturn(Optional.of(payment));
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        Payment updated = paymentService.setStatus(payment, "SUCCESS");
        assertEquals("SUCCESS", updated.getStatus());
        assertEquals("SUCCESS", order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new Payment("p1", order.getId(), "Cash on Delivery", "PENDING", validCodData);
        when(paymentRepository.findById("p1")).thenReturn(Optional.of(payment));
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        Payment updated = paymentService.setStatus(payment, "REJECTED");
        assertEquals("REJECTED", updated.getStatus());
        assertEquals("FAILED", order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void testSetStatusInvalid() {
        Payment payment = new Payment("p1", order.getId(), "Cash on Delivery", "PENDING", validCodData);
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.setStatus(payment, "INVALID");
        });
    }

    @Test
    void testGetPaymentValid() {
        Payment payment = new Payment("p1", order.getId(), "Cash on Delivery", "PENDING", validCodData);
        when(paymentRepository.findById("p1")).thenReturn(Optional.of(payment));
        Payment result = paymentService.getPayment("p1");
        assertEquals("p1", result.getId());
    }

    @Test
    void testGetPaymentInvalid() {
        when(paymentRepository.findById("p1")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            paymentService.getPayment("p1");
        });
    }

    @Test
    void testGetAllPayments() {
        List<Payment> list = new ArrayList<>();
        when(paymentRepository.findAll()).thenReturn(list);
        assertEquals(list, paymentService.getAllPayments());
    }
}