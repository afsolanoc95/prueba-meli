package com.hackerrank.sample.integration;

import com.hackerrank.sample.Application;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.model.Seller;
import com.hackerrank.sample.repository.SellerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Base class for integration tests using H2 in-memory database. Provides shared
 * configuration and utility methods for all integration tests. H2 runs in
 * PostgreSQL compatibility mode for realistic testing without Docker.
 */
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected SellerRepository sellerRepository;

    protected Seller testSeller;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUpTestData() {
        // Create a test seller for use in tests
        testSeller = Seller.builder().name("Test Seller").reputation(95).totalSales(1000).yearsActive(5)
                .responseTime("2 hours").build();
        testSeller = sellerRepository.save(testSeller);
    }

    /**
     * Helper method to create a basic product for testing
     */
    protected Product.ProductBuilder createBasicProductBuilder() {
        return Product.builder().title("Test Product").price(new BigDecimal("99.99"))
                .originalPrice(new BigDecimal("149.99")).currency("USD").availableQuantity(10).soldQuantity(0)
                .condition("new").description("Test product description").warranty("1 year").seller(testSeller);
    }

    protected <T> T readJson(String path, Class<T> clazz) throws IOException {
        return objectMapper.readValue(new ClassPathResource("fixtures/" + path).getInputStream(), clazz);
    }

    protected <T> List<T> readJsonList(String path, Class<T> clazz) throws IOException {
        return objectMapper.readValue(new ClassPathResource("fixtures/" + path).getInputStream(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    protected String readJsonString(String path) throws IOException {
        return StreamUtils.copyToString(new ClassPathResource("fixtures/" + path).getInputStream(),
                StandardCharsets.UTF_8);
    }
}
