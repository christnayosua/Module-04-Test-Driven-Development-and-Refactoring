package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProductPage() {
        String viewName = productController.createProductPage(model);

        verify(model).addAttribute(eq("product"), any(Product.class));
        assertEquals("CreateProduct", viewName);
    }

    @Test
    void testCreateProductPost() {
        Product product = new Product();

        String viewName =
                productController.createProductPost(product, model);

        verify(productService).create(product);
        assertEquals("redirect:/product/list", viewName);
    }

    @Test
    void testProductListPage() {
        when(productService.findAll()).thenReturn(List.of(new Product()));

        String viewName = productController.productListPage(model);

        verify(model).addAttribute(eq("products"), anyList());
        assertEquals("ProductList", viewName);
    }

    @Test
    void testEditProductPageFound() {
        Product product = new Product();
        when(productService.findById("1")).thenReturn(product);

        String viewName =
                productController.editProductPage("1", model);

        verify(model).addAttribute("product", product);
        assertEquals("editProduct", viewName);
    }

    @Test
    void testEditProductPageNotFound() {
        when(productService.findById("1")).thenReturn(null);

        String viewName =
                productController.editProductPage("1", model);

        assertEquals("redirect:/product/list", viewName);
    }

    @Test
    void testEditProductPost() {
        Product product = new Product();

        String viewName =
                productController.editProductPost(product, model);

        verify(productService).update(product);
        assertEquals("redirect:/product/list", viewName);
    }

    @Test
    void testDeleteProductSuccess() {
        when(productService.delete("1")).thenReturn(true);

        String viewName =
                productController.deleteProduct("1", model);

        verify(model, never()).addAttribute(anyString(), any());
        assertEquals("redirect:/product/list", viewName);
    }

    @Test
    void testDeleteProductFail() {
        when(productService.delete("1")).thenReturn(false);

        String viewName =
                productController.deleteProduct("1", model);

        verify(model).addAttribute("errorMessage", "Product not found!");
        assertEquals("redirect:/product/list", viewName);
    }
}