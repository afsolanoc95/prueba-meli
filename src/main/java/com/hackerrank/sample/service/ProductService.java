package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.CreateProductRequest;
import com.hackerrank.sample.dto.ProductDetailDTO;
import com.hackerrank.sample.dto.ProductSummaryDTO;
import com.hackerrank.sample.dto.UpdateProductRequest;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.mapper.ProductMapper;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.model.ProductAttribute;
import com.hackerrank.sample.model.ProductImage;
import com.hackerrank.sample.model.Seller;
import com.hackerrank.sample.repository.ProductRepository;
import com.hackerrank.sample.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> getAllProducts() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toSummaryDTO).toList();
    }

    @Transactional(readOnly = true)
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "productCB")
    public ProductDetailDTO getProductDetail(Long id) {
        log.info("Fetching product by id: {}", id);
        Product product = productRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new NoSuchResourceFoundException("Product not found with id: " + id));

        return productMapper.toDetailDTO(product);
    }

    // CREATE methods
    @Transactional
    public ProductDetailDTO createProduct(CreateProductRequest request) {
        log.info("Attempting to create product: {}", request.getTitle());
        // Validate seller exists
        Seller seller = sellerRepository.findById(request.getSellerId()).orElseThrow(
                () -> new NoSuchResourceFoundException("Seller not found with id: " + request.getSellerId()));

        // Create product using Builder
        Product product = Product.builder().title(request.getTitle()).price(request.getPrice())
                .originalPrice(request.getOriginalPrice()).currency(request.getCurrency())
                .availableQuantity(request.getAvailableQuantity()).soldQuantity(0).condition(request.getCondition())
                .description(request.getDescription()).warranty(request.getWarranty()).seller(seller)
                // Images and Attributes will be added below to handle bidirectional
                // relationship correctly if helper methods are used
                // Or we can build them if we ensure relationship is set.
                // Using helper methods is safer for bidirectional JPA.
                .build();

        // Add images if provided
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            for (int i = 0; i < request.getImageUrls().size(); i++) {
                boolean isPrimary = (i == 0); // First image is primary
                product.addImage(new ProductImage(request.getImageUrls().get(i), isPrimary));
            }
        }

        // Add attributes if provided
        if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
            for (CreateProductRequest.AttributeRequest attr : request.getAttributes()) {
                product.addAttribute(new ProductAttribute(attr.getName(), attr.getValue()));
            }
        }

        // Save product
        Product savedProduct = productRepository.save(product);

        // Return as DTO
        return productMapper.toDetailDTO(savedProduct);
    }

    // UPDATE methods
    @Transactional
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "productCB")
    public ProductDetailDTO updateProduct(Long id, UpdateProductRequest request) {
        // Find product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchResourceFoundException("Product not found with id: " + id));

        // Dynamic Update (OCP compliant): Copies all non-null fields from request to
        // product
        // This avoids modifying this method when new scalar fields are added to
        // Product/Request.
        copyNonNullProperties(request, product);

        // Handle Collections/Relationships explicitly if they require special logic
        // (e.g., clear and add)
        // Images
        if (request.getImageUrls() != null) {
            product.getImages().clear();
            for (int i = 0; i < request.getImageUrls().size(); i++) {
                boolean isPrimary = (i == 0);
                product.addImage(new ProductImage(request.getImageUrls().get(i), isPrimary));
            }
        }

        // Attributes
        if (request.getAttributes() != null) {
            product.getAttributes().clear();
            for (UpdateProductRequest.AttributeRequest attr : request.getAttributes()) {
                product.addAttribute(new ProductAttribute(attr.getName(), attr.getValue()));
            }
        }

        // Save and return
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDetailDTO(updatedProduct);
    }

    // Helper method for OCP-compliant partial updates
    private void copyNonNullProperties(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private String[] getNullPropertyNames(Object source) {
        final org.springframework.beans.BeanWrapper src = new org.springframework.beans.BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        java.util.Set<String> emptyNames = new java.util.HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    // DELETE methods
    @Transactional
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "productCB")
    public void deleteProduct(Long id) {
        // Validate product exists
        if (!productRepository.existsById(id)) {
            throw new NoSuchResourceFoundException("Product not found with id: " + id);
        }

        // Delete (cascade will delete images, attributes, reviews, questions)
        productRepository.deleteById(id);
    }
}
