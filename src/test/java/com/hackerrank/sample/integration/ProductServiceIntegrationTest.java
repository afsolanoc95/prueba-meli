package com.hackerrank.sample.integration;

import com.hackerrank.sample.dto.CreateProductRequest;
import com.hackerrank.sample.dto.ProductDetailDTO;
import com.hackerrank.sample.dto.ProductSummaryDTO;
import com.hackerrank.sample.dto.UpdateProductRequest;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import com.hackerrank.sample.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for ProductService covering complete CRUD lifecycle
 */
@Transactional
class ProductServiceIntegrationTest extends BaseIntegrationTest {

        @Autowired
        private ProductService productService;

        @Autowired
        private ProductRepository productRepository;

        @Test
        @DisplayName("Should complete product lifecycle: create -> get -> update -> delete")
        void testCompleteProductLifecycle() throws IOException {
                // 1. CREATE: Create product with images and attributes
                CreateProductRequest createRequest = readJson("product/create-product-request.json",
                                CreateProductRequest.class);
                createRequest.setTitle("Samsung Galaxy S24");
                createRequest.setPrice(new BigDecimal("999.99"));
                createRequest.setOriginalPrice(new BigDecimal("1199.99"));
                createRequest.setDescription("Latest Samsung flagship");
                createRequest.setImageUrls(
                                Arrays.asList("https://example.com/img1.jpg", "https://example.com/img2.jpg"));
                createRequest.setSellerId(testSeller.getId());

                CreateProductRequest.AttributeRequest attr1 = new CreateProductRequest.AttributeRequest();
                attr1.setName("Color");
                attr1.setValue("Black");
                CreateProductRequest.AttributeRequest attr2 = new CreateProductRequest.AttributeRequest();
                attr2.setName("Storage");
                attr2.setValue("256GB");
                createRequest.setAttributes(Arrays.asList(attr1, attr2));

                ProductDetailDTO created = productService.createProduct(createRequest);

                assertThat(created).isNotNull().extracting(ProductDetailDTO::getTitle, ProductDetailDTO::getPrice)
                                .containsExactly("Samsung Galaxy S24", new BigDecimal("999.99"));
                assertThat(created.getImages()).hasSize(2);
                assertThat(created.getAttributes()).hasSize(2);
                assertThat(created.getSeller().getName()).isEqualTo("Test Seller");

                Long productId = created.getId();

                // 2. GET BY ID: Retrieve product detail
                ProductDetailDTO retrieved = productService.getProductDetail(productId);

                assertThat(retrieved).isNotNull();
                assertThat(retrieved.getId()).isEqualTo(productId);
                assertThat(retrieved.getTitle()).isEqualTo("Samsung Galaxy S24");

                // 3. GET ALL: List all products
                List<ProductSummaryDTO> allProducts = productService.getAllProducts();

                assertThat(allProducts).isNotEmpty().anyMatch(p -> p.getId().equals(productId));

                // 4. UPDATE: Update product
                UpdateProductRequest updateRequest = readJson("product/update-product-request.json",
                                UpdateProductRequest.class);
                updateRequest.setPrice(new BigDecimal("899.99"));
                updateRequest.setAvailableQuantity(45);

                ProductDetailDTO updated = productService.updateProduct(productId, updateRequest);

                assertThat(updated.getPrice()).isEqualByComparingTo(new BigDecimal("899.99"));
                assertThat(updated.getAvailableQuantity()).isEqualTo(45);
                assertThat(updated.getTitle()).isEqualTo("Samsung Galaxy S24"); // Unchanged

                // 5. DELETE: Delete product
                productService.deleteProduct(productId);

                // 6. VERIFY DELETED: Confirm product no longer exists
                assertThatThrownBy(() -> productService.getProductDetail(productId))
                                .isInstanceOf(NoSuchResourceFoundException.class)
                                .hasMessageContaining("Product not found");
        }

        @Test
        @DisplayName("Should throw exception when getting non-existent product")
        void testGetNonExistentProduct_ShouldThrowException() {
                Long nonExistentId = 99999L;

                assertThatThrownBy(() -> productService.getProductDetail(nonExistentId))
                                .isInstanceOf(NoSuchResourceFoundException.class)
                                .hasMessageContaining("Product not found with id: " + nonExistentId);
        }

        @Test
        @DisplayName("Should throw exception when creating product with invalid seller")
        void testCreateProductWithInvalidSeller_ShouldThrowException() throws IOException {
                CreateProductRequest request = readJson("product/create-product-request.json",
                                CreateProductRequest.class);
                request.setSellerId(99999L); // Non-existent seller

                assertThatThrownBy(() -> productService.createProduct(request))
                                .isInstanceOf(NoSuchResourceFoundException.class)
                                .hasMessageContaining("Seller not found");
        }

        @Test
        @DisplayName("Should update only specified fields (partial update)")
        void testUpdateProduct_PartialUpdate() throws IOException {
                // Create product
                Product product = createBasicProductBuilder().build();
                product = productRepository.save(product);

                String originalTitle = product.getTitle();
                Integer originalQuantity = product.getAvailableQuantity();

                // Update only price
                UpdateProductRequest updateRequest = readJson("product/update-product-request.json",
                                UpdateProductRequest.class);
                updateRequest.setPrice(new BigDecimal("79.99"));

                ProductDetailDTO updated = productService.updateProduct(product.getId(), updateRequest);

                // Verify only price changed
                assertThat(updated.getPrice()).isEqualByComparingTo(new BigDecimal("79.99"));
                assertThat(updated.getTitle()).isEqualTo(originalTitle);
                assertThat(updated.getAvailableQuantity()).isEqualTo(originalQuantity);
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent product")
        void testUpdateNonExistentProduct_ShouldThrowException() throws IOException {
                UpdateProductRequest updateRequest = readJson("product/update-product-request.json",
                                UpdateProductRequest.class);
                updateRequest.setPrice(new BigDecimal("99.99"));

                assertThatThrownBy(() -> productService.updateProduct(99999L, updateRequest))
                                .isInstanceOf(NoSuchResourceFoundException.class)
                                .hasMessageContaining("Product not found");
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent product")
        void testDeleteNonExistentProduct_ShouldThrowException() {
                assertThatThrownBy(() -> productService.deleteProduct(99999L))
                                .isInstanceOf(NoSuchResourceFoundException.class)
                                .hasMessageContaining("Product not found");
        }
}
