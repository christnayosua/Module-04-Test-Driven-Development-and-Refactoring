package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import java.util.*;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String id = UUID.randomUUID().toString();
        String status = "PENDING";

        if ("Cash on Delivery".equals(method)) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");
            if (address == null || address.trim().isEmpty() ||
                deliveryFee == null || deliveryFee.trim().isEmpty()) {
                status = "REJECTED";
            }
        }

        Payment payment = new Payment(id, order.getId(), method, status, paymentData);
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        if (!status.equals("SUCCESS") && !status.equals("REJECTED")) {
            throw new IllegalArgumentException("Invalid status");
        }
        payment.setStatus(status);
        paymentRepository.save(payment);

        Order order = orderRepository.findById(payment.getOrderId());
        if (order == null) {
            throw new NoSuchElementException("Order not found");
        }
        if ("SUCCESS".equals(status)) {
            order.setStatus("SUCCESS");
        } else if ("REJECTED".equals(status)) {
            order.setStatus("FAILED");
        }
        orderRepository.save(order);
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment not found"));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}