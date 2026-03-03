package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryImplTest {

    private ProductRepositoryImpl productRepository;

    private Product createTestProduct(String id, String name, int quantity) {
        Product product = new Product();
        product.setProductId(id);
        product.setProductName(name);
        product.setProductQuantity(quantity);
        return product;
    }

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepositoryImpl();
    }

    @Test
    void testCreateProductWithExistingId() {
        // Arrange
        String existingId = "existing-id-123";
        Product product = createTestProduct(existingId, "Product with ID", 10);

        // Act
        Product createdProduct = productRepository.create(product);

        // Assert
        assertNotNull(createdProduct);
        assertEquals(existingId, createdProduct.getProductId());
        assertEquals("Product with ID", createdProduct.getProductName());
        assertEquals(10, createdProduct.getProductQuantity());

        // Verify it was added to the list
        Iterator<Product> iterator = productRepository.findAll();
        assertTrue(iterator.hasNext());
    }

    @Test
    void testCreateProductWithoutId() {
        // Arrange
        Product product = new Product();
        product.setProductName("Product without ID");
        product.setProductQuantity(20);

        // Act
        Product createdProduct = productRepository.create(product);

        // Assert
        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getProductId());
        assertFalse(createdProduct.getProductId().isEmpty());
        assertEquals("Product without ID", createdProduct.getProductName());
        assertEquals(20, createdProduct.getProductQuantity());
    }

    @Test
    void testCreateProductWithEmptyId() {
        // Arrange
        Product product = new Product();
        product.setProductId("");
        product.setProductName("Product with empty ID");
        product.setProductQuantity(30);

        // Act
        Product createdProduct = productRepository.create(product);

        // Assert
        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getProductId());
        assertFalse(createdProduct.getProductId().isEmpty()); // Should be regenerated
        assertEquals("Product with empty ID", createdProduct.getProductName());
        assertEquals(30, createdProduct.getProductQuantity());
    }

    @Test
    void testFindAllWhenEmpty() {
        // Act
        Iterator<Product> iterator = productRepository.findAll();

        // Assert
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindAllWithMultipleProducts() {
        // Arrange
        Product product1 = createTestProduct("id-1", "Product 1", 10);
        Product product2 = createTestProduct("id-2", "Product 2", 20);
        Product product3 = createTestProduct("id-3", "Product 3", 30);

        productRepository.create(product1);
        productRepository.create(product2);
        productRepository.create(product3);

        // Act
        Iterator<Product> iterator = productRepository.findAll();

        // Assert
        assertNotNull(iterator);

        // Count products
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void testFindByIdWhenProductExists() {
        // Arrange
        Product product = createTestProduct("test-id", "Test Product", 100);
        productRepository.create(product);

        // Act
        Product foundProduct = productRepository.findById("test-id");

        // Assert
        assertNotNull(foundProduct);
        assertEquals("test-id", foundProduct.getProductId());
        assertEquals("Test Product", foundProduct.getProductName());
        assertEquals(100, foundProduct.getProductQuantity());
    }

    @Test
    void testFindByIdWhenProductDoesNotExist() {
        // Act
        Product foundProduct = productRepository.findById("non-existent-id");

        // Assert
        assertNull(foundProduct);
    }

    @Test
    void testFindByIdWithNullId() {
        // Act
        Product foundProduct = productRepository.findById(null);

        // Assert
        assertNull(foundProduct);
    }

    @Test
    void testUpdateProductSuccess() {
        // Arrange
        Product originalProduct = createTestProduct("update-id", "Original Name", 10);
        productRepository.create(originalProduct);

        Product updatedProduct = createTestProduct("update-id", "Updated Name", 20);

        // Act
        Product result = productRepository.update(updatedProduct);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getProductName());
        assertEquals(20, result.getProductQuantity());

        // Verify the product in repository is updated
        Product retrievedProduct = productRepository.findById("update-id");
        assertNotNull(retrievedProduct);
        assertEquals("Updated Name", retrievedProduct.getProductName());
        assertEquals(20, retrievedProduct.getProductQuantity());
    }

    @Test
    void testUpdateProductNotFound() {
        // Arrange
        Product product = createTestProduct("non-existent-id", "Test Product", 10);

        // Act
        Product result = productRepository.update(product);

        // Assert
        assertNull(result);
    }

    @Test
    void testUpdateProductWithNullProduct() {
        // Act
        Product result = productRepository.update(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testUpdateProductWithNullId() {
        // Arrange
        Product product = new Product();
        product.setProductId(null);
        product.setProductName("Product with null ID");
        product.setProductQuantity(10);

        // Act
        Product result = productRepository.update(product);

        // Assert
        assertNull(result);
    }

    @Test
    void testDeleteProductSuccess() {
        // Arrange
        Product product = createTestProduct("delete-id", "Product to Delete", 5);
        productRepository.create(product);

        // Verify product exists before deletion
        assertNotNull(productRepository.findById("delete-id"));

        // Act
        boolean deletionResult = productRepository.delete("delete-id");

        // Assert
        assertTrue(deletionResult);
        assertNull(productRepository.findById("delete-id"));
    }

    @Test
    void testDeleteProductNotFound() {
        // Act
        boolean deletionResult = productRepository.delete("non-existent-id");

        // Assert
        assertFalse(deletionResult);
    }

    @Test
    void testDeleteProductWithNullId() {
        // Act
        boolean deletionResult = productRepository.delete(null);

        // Assert
        assertFalse(deletionResult);
    }

    @Test
    void testDeleteProductWithEmptyId() {
        // Arrange
        Product product = createTestProduct("", "Product with empty ID", 10);
        productRepository.create(product);        

        // Act
        boolean deletionResult = productRepository.delete("");

        // Assert
        assertFalse(deletionResult); // Karena ID kosong, tidak akan menemukan produk
        // Namun produk dengan ID kosong tetap ada? Sebenarnya ID kosong disimpan, tapi findById dengan "" akan mengembalikan null karena perbandingan equals("") mungkin? Tergantung implementasi. Tapi di test sebelumnya, findById("") mengembalikan null. Jadi delete("") seharusnya false.
    }

    @Test
    void testMultipleOperations() {
        // Test create multiple products
        Product p1 = createTestProduct("id-1", "Product 1", 1);
        Product p2 = createTestProduct("id-2", "Product 2", 2);
        Product p3 = createTestProduct("id-3", "Product 3", 3);

        productRepository.create(p1);
        productRepository.create(p2);
        productRepository.create(p3);

        // Test findAll
        Iterator<Product> iterator = productRepository.findAll();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(3, count);

        // Test update
        Product updatedP2 = createTestProduct("id-2", "Updated Product 2", 20);
        productRepository.update(updatedP2);

        Product foundP2 = productRepository.findById("id-2");
        assertEquals("Updated Product 2", foundP2.getProductName());
        assertEquals(20, foundP2.getProductQuantity());

        // Test delete
        boolean deleted = productRepository.delete("id-1");
        assertTrue(deleted);

        // Verify deletion
        assertNull(productRepository.findById("id-1"));

        // Verify other products still exist
        assertNotNull(productRepository.findById("id-2"));
        assertNotNull(productRepository.findById("id-3"));

        // Test iterator after deletion
        iterator = productRepository.findAll();
        count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void testCreateProductWithNullName() {
        // Test edge case for product with null name
        Product product = new Product();
        product.setProductId("null-name-id");
        product.setProductName(null);
        product.setProductQuantity(10);

        Product created = productRepository.create(product);

        assertNotNull(created);
        assertNull(created.getProductName());
        assertEquals(10, created.getProductQuantity());
    }
}