package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private Map<String,String> validCodData;
    private Map<String,String> invalidCodData;

    @BeforeEach
    void setUp(){

        Product product = new Product();
        product.setProductId("prod1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);

        List<Product> products = new ArrayList<>();
        products.add(product);

        order = new Order("order1", products, 1708560000L, "Safira Sudrajat");

        validCodData = new HashMap<>();
        validCodData.put("address","Jl. Merdeka No. 1");
        validCodData.put("deliveryFee","15000");

        invalidCodData = new HashMap<>();
        invalidCodData.put("address","");
        invalidCodData.put("deliveryFee","15000");

        lenient().when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void testAddPaymentCashOnDeliveryValid(){

        Payment result = paymentService.addPayment(order,"Cash on Delivery",validCodData);

        assertEquals("SUCCESS",result.getStatus());
        assertEquals(order.getId(),result.getOrderId());

        verify(paymentRepository,times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCashOnDeliveryInvalidAddress(){

        Payment result = paymentService.addPayment(order,"Cash on Delivery",invalidCodData);

        assertEquals("REJECTED",result.getStatus());

        verify(paymentRepository,times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCashOnDeliveryMissingFee(){

        Map<String,String> missingFee = new HashMap<>();
        missingFee.put("address","Jl. A");

        Payment result = paymentService.addPayment(order,"Cash on Delivery",missingFee);

        assertEquals("REJECTED",result.getStatus());

        verify(paymentRepository,times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherValid(){

        Map<String,String> voucherData = new HashMap<>();
        voucherData.put("voucherCode","ESHOP12345678ABC");

        Payment result = paymentService.addPayment(order,"Voucher",voucherData);

        assertEquals("SUCCESS",result.getStatus());

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherInvalidPrefix(){

        Map<String,String> voucherData = new HashMap<>();
        voucherData.put("voucherCode","INVALID12345678AB");

        Payment result = paymentService.addPayment(order,"Voucher",voucherData);

        assertEquals("REJECTED",result.getStatus());

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherInvalidLength(){

        Map<String,String> voucherData = new HashMap<>();
        voucherData.put("voucherCode","ESHOP123");

        Payment result = paymentService.addPayment(order,"Voucher",voucherData);

        assertEquals("REJECTED",result.getStatus());

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherInvalidDigitCount(){

        Map<String,String> voucherData = new HashMap<>();
        voucherData.put("voucherCode","ESHOPABCDEFGH123");

        Payment result = paymentService.addPayment(order,"Voucher",voucherData);

        assertEquals("REJECTED",result.getStatus());

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess(){

        Payment payment = new Payment("p1",order.getId(),"Cash on Delivery","PENDING",validCodData);

        when(orderRepository.findById(order.getId())).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        Payment result = paymentService.setStatus(payment,"SUCCESS");

        assertEquals("SUCCESS",result.getStatus());
        assertEquals("SUCCESS",order.getStatus());

        verify(paymentRepository).save(payment);
        verify(orderRepository).save(order);
    }

    @Test
    void testSetStatusRejected(){

        Payment payment = new Payment("p1",order.getId(),"Cash on Delivery","PENDING",validCodData);

        when(orderRepository.findById(order.getId())).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        Payment result = paymentService.setStatus(payment,"REJECTED");

        assertEquals("REJECTED",result.getStatus());
        assertEquals("FAILED",order.getStatus());

        verify(paymentRepository).save(payment);
        verify(orderRepository).save(order);
    }

    @Test
    void testSetStatusInvalid(){

        Payment payment = new Payment("p1",order.getId(),"Cash on Delivery","PENDING",validCodData);

        assertThrows(IllegalArgumentException.class,
                () -> paymentService.setStatus(payment,"INVALID"));

        verify(paymentRepository,never()).save(any());
        verify(orderRepository,never()).findById(any());
    }

    @Test
    void testGetPaymentFound(){

        Payment payment = new Payment("p1",order.getId(),"Cash on Delivery","PENDING",validCodData);

        when(paymentRepository.findById("p1")).thenReturn(Optional.of(payment));

        Payment result = paymentService.getPayment("p1");

        assertEquals("p1",result.getId());
    }

    @Test
    void testGetPaymentNotFound(){

        when(paymentRepository.findById("p1")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> paymentService.getPayment("p1"));
    }

    @Test
    void testGetAllPayments(){

        List<Payment> payments = new ArrayList<>();

        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(payments,result);
    }
}