package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final List<Product> productData = new ArrayList<>();

    @Override
    public Product create(Product product) {
        if (product.getProductId() == null || product.getProductId().isEmpty()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        productData.add(product);
        return product;
    }

    @Override
    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    @Override
    public Product findById(String productId) {
        for (Product product : productData) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    @Override
    public Product update(Product updatedProduct) {
        if (updatedProduct == null || updatedProduct.getProductId() == null) {
            return null;
        }
        for (int i = 0; i < productData.size(); i++) {
            Product product = productData.get(i);
            if (product.getProductId().equals(updatedProduct.getProductId())) {
                productData.set(i, updatedProduct);
                return updatedProduct;
            }
        }
        return null;
    }

    @Override
    public boolean delete(String productId) {
        return productData.removeIf(product -> product.getProductId().equals(productId));
    }
}