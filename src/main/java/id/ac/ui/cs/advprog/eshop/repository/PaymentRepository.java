package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentRepository {
    private List<Payment> payments = new ArrayList<>();

    public void save(Payment payment) {
        Optional<Payment> existing = findById(payment.getId());
        if (existing.isPresent()) {
            int index = payments.indexOf(existing.get());
            payments.set(index, payment);
        } else {
            payments.add(payment);
        }
    }

    public Optional<Payment> findById(String id) {
        return payments.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<Payment> findAll() {
        return new ArrayList<>(payments);
    }
}