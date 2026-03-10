package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final String ADDRESS_KEY = "address";
    private static final String DELIVERY_FEE_KEY = "deliveryFee";
    private static final String SAMPLE_ADDRESS = "Sample Address";
    private static final String SAMPLE_DELIVERY_FEE = "10000";

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/create")
    public String createOrderPage() {
        return "order/create";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam String author) {

        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Sample Product");
        product.setProductQuantity(1);

        List<Product> products = Collections.singletonList(product);

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
            @RequestParam String method
    ) {

        Order order = orderService.findById(orderId);

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put(ADDRESS_KEY, SAMPLE_ADDRESS);
        paymentData.put(DELIVERY_FEE_KEY, SAMPLE_DELIVERY_FEE);

        Payment payment = paymentService.addPayment(
                order,
                method,
                paymentData
        );

        return "redirect:/payment/detail/" + payment.getId();
    }
}