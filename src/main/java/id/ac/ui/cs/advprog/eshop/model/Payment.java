package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payment {
    private String id;
    private String orderId;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String orderId, String method, Map<String, String> paymentData) {
        this.id = id;
        this.orderId = orderId;
        this.method = method;
        this.paymentData = paymentData;
        this.status = "PENDING";
    }

    public Payment(String id, String orderId, String method, String status, Map<String, String> paymentData) {
        this.id = id;
        this.orderId = orderId;
        this.method = method;
        this.status = status;
        this.paymentData = paymentData;
    }
}