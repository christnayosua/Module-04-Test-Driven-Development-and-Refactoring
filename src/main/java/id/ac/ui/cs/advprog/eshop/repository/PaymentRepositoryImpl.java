package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentRepositoryImpl implements PaymentRepository {

    private final List<Payment> payments = new ArrayList<>();

    @Override
    public Payment save(Payment payment) {
        Optional<Payment> existingPayment = findById(payment.getId());

        if (existingPayment.isPresent()) {
            int index = payments.indexOf(existingPayment.get());
            payments.set(index, payment);
        } else {
            payments.add(payment);
        }

        return payment;
    }

    @Override
    public Optional<Payment> findById(String id) {
        return payments
                .stream()
                .filter(payment -> payment.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(payments);
    }
}