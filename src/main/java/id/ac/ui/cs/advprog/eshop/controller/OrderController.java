package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "order/create";
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
            Model model
    ) {

        String paymentId = UUID.randomUUID().toString();

        model.addAttribute("paymentId", paymentId);

        return "payment/detail";
    }

}