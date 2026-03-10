package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_REJECTED = "REJECTED";

    private static final String METHOD_COD = "Cash on Delivery";

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {

        String id = UUID.randomUUID().toString();

        Payment payment = new Payment(id, order.getId(), method, paymentData);

        if (METHOD_COD.equals(method)) {
            validateCashOnDelivery(payment, paymentData);
        }

        paymentRepository.save(payment);

        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {

        if (!STATUS_SUCCESS.equals(status) && !STATUS_REJECTED.equals(status)) {
            throw new IllegalArgumentException("Invalid payment status");
        }

        payment.setStatus(status);
        paymentRepository.save(payment);

        Order order = orderRepository.findById(payment.getOrderId());

        if (order == null) {
            throw new NoSuchElementException("Order not found");
        }

        updateOrderStatus(order, status);

        orderRepository.save(order);

        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {

        return paymentRepository
                .findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment not found"));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private void validateCashOnDelivery(Payment payment, Map<String, String> paymentData) {

        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");

        if (isEmpty(address) || isEmpty(deliveryFee)) {
            payment.setStatus(STATUS_REJECTED);
        }
    }

    private void updateOrderStatus(Order order, String paymentStatus) {

        if (STATUS_SUCCESS.equals(paymentStatus)) {
            order.setStatus("SUCCESS");
        }

        if (STATUS_REJECTED.equals(paymentStatus)) {
            order.setStatus("FAILED");
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}