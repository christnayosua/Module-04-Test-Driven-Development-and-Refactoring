package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "order/create";
    }

    @PostMapping("/create")
    public String createOrder(
            @RequestParam String author
    ) {

        List<Product> products = new ArrayList<>();

        Product dummyProduct = new Product();
        dummyProduct.setProductId(UUID.randomUUID().toString());
        dummyProduct.setProductName("Sample Product");
        dummyProduct.setProductQuantity(1);

        products.add(dummyProduct);

        Order order = new Order(
                UUID.randomUUID().toString(),
                products,
                System.currentTimeMillis(),
                author
        );

        orderService.createOrder(order);

        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String historyPage() {
        return "order/history";
    }

    @PostMapping("/history")
    public String orderHistory(
            @RequestParam String author,
            Model model
    ) {

        List<Order> orders = orderService.findAllByAuthor(author);

        model.addAttribute("orders", orders);

        return "order/history";
    }

    @GetMapping("/pay/{orderId}")
    public String payPage(
            @PathVariable String orderId,
            Model model
    ) {

        model.addAttribute("orderId", orderId);

        return "order/pay";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrder(
            @PathVariable String orderId,
            @RequestParam String method,
            @RequestParam(required = false) String voucherCode,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String deliveryFee
    ) {

        Order order = orderService.findById(orderId);

        Map<String, String> paymentData = new HashMap<>();

        if ("Voucher".equals(method)) {
            paymentData.put("voucherCode", voucherCode);
        }

        if ("Cash on Delivery".equals(method)) {
            paymentData.put("address", address);
            paymentData.put("deliveryFee", deliveryFee);
        }

        Payment payment = paymentService.addPayment(
                order,
                method,
                paymentData
        );

        return "redirect:/payment/detail/" + payment.getId();
    }
}