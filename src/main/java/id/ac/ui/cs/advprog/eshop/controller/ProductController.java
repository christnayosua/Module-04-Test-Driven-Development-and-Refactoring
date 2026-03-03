package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService service;

    // Constructor Injection 
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model) {
        service.create(product);
        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "ProductList";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable("id") String id, Model model) {

        Product product = service.findById(id);

        if (product == null) {
            return "redirect:/product/list";
        }

        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/edit")
    public String editProductPost(@ModelAttribute Product product, Model model) {
        service.update(product);
        return "redirect:/product/list";
    }

    @PostMapping("/delete")
    public String deleteProduct(
            @RequestParam("productId") String productId,
            Model model) {

        boolean deleted = service.delete(productId);

        if (!deleted) {
            model.addAttribute("errorMessage", "Product not found!");
        }

        return "redirect:/product/list";
    }
}