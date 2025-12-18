package com.hackerrank.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.dto.*;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.security.AuthEntryPointJwt;
import com.hackerrank.sample.security.AuthTokenFilter;
import com.hackerrank.sample.security.CustomUserDetailsService;
import com.hackerrank.sample.service.ProductService;
import com.hackerrank.sample.service.QuestionService;
import com.hackerrank.sample.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller tests for ProductController using MockMvc Tests all endpoints:
 * products, reviews, and questions Uses JSON fixtures for test data
 */
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for unit tests
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private QuestionService questionService;

    // Security Mocks needed for SecurityConfig to load
    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private AuthTokenFilter authTokenFilter;

    // Helper methods to read JSON fixtures
    private <T> T readJson(String path, Class<T> clazz) throws IOException {
        return objectMapper.readValue(new ClassPathResource("fixtures/" + path).getInputStream(), clazz);
    }

    private <T> List<T> readJsonList(String path, Class<T> clazz) throws IOException {
        return objectMapper.readValue(new ClassPathResource("fixtures/" + path).getInputStream(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    private String readJsonString(String path) throws IOException {
        return StreamUtils.copyToString(new ClassPathResource("fixtures/" + path).getInputStream(),
                StandardCharsets.UTF_8);
    }

    // =================== PRODUCT ENDPOINTS ===================

    @Test
    @DisplayName("GET /api/products - Should return all products")
    void testGetAllProducts() throws Exception {
        List<ProductSummaryDTO> products = readJsonList("product/get-all-products.json", ProductSummaryDTO.class);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[0].title").value("Product 1"));

        verify(productService).getAllProducts();
    }

    @Test
    @DisplayName("GET /api/products/{id} - Should return product detail")
    void testGetProductById() throws Exception {
        ProductDetailDTO product = readJson("product/get-product-by-id.json", ProductDetailDTO.class);

        when(productService.getProductDetail(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Samsung Galaxy S24"));

        verify(productService).getProductDetail(1L);
    }

    @Test
    @DisplayName("GET /api/products/{id} - Should return 404 when product not found")
    void testGetProductById_NotFound() throws Exception {
        when(productService.getProductDetail(99999L))
                .thenThrow(new NoSuchResourceFoundException("Product not found with id: 99999"));

        mockMvc.perform(get("/api/products/99999")).andExpect(status().isNotFound());

        verify(productService).getProductDetail(99999L);
    }

    @Test
    @DisplayName("POST /api/products - Should create new product")
    void testCreateProduct() throws Exception {
        readJson("product/create-product-request.json", CreateProductRequest.class);
        ProductDetailDTO created = readJson("product/create-product-response.json", ProductDetailDTO.class);
        String requestBody = readJsonString("product/create-product-request.json");

        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(created);

        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Product"));

        verify(productService).createProduct(any(CreateProductRequest.class));
    }

    @Test
    @DisplayName("PUT /api/products/{id} - Should update product")
    void testUpdateProduct() throws Exception {
        ProductDetailDTO updated = readJson("product/update-product-response.json", ProductDetailDTO.class);
        String requestBody = readJsonString("product/update-product-request.json");

        when(productService.updateProduct(eq(1L), any(UpdateProductRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/products/1").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.price").value(179.99));

        verify(productService).updateProduct(eq(1L), any(UpdateProductRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Should delete product")
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1")).andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    // =================== REVIEW ENDPOINTS ===================

    @Test
    @DisplayName("GET /api/products/{id}/reviews - Should return all reviews")
    void testGetProductReviews() throws Exception {
        List<ReviewDTO> reviews = readJsonList("review/get-product-reviews.json", ReviewDTO.class);

        when(reviewService.getProductReviews(1L)).thenReturn(reviews);

        mockMvc.perform(get("/api/products/1/reviews")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].rating").value(5));

        verify(reviewService).getProductReviews(1L);
    }

    @Test
    @DisplayName("POST /api/products/{id}/reviews - Should create review")
    void testAddReview() throws Exception {
        ReviewDTO created = readJson("review/create-review-response.json", ReviewDTO.class);
        String requestBody = readJsonString("review/create-review-request.json");

        when(reviewService.addReview(eq(1L), any(CreateReviewRequest.class))).thenReturn(created);

        mockMvc.perform(post("/api/products/1/reviews").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.rating").value(5));

        verify(reviewService).addReview(eq(1L), any(CreateReviewRequest.class));
    }

    @Test
    @DisplayName("PUT /api/products/{productId}/reviews/{reviewId} - Should update review")
    void testUpdateReview() throws Exception {
        ReviewDTO updated = readJson("review/update-review-response.json", ReviewDTO.class);
        String requestBody = readJsonString("review/update-review-request.json");

        when(reviewService.updateReview(eq(1L), eq(1L), any(UpdateReviewRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/products/1/reviews/1").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andExpect(jsonPath("$.rating").value(4));

        verify(reviewService).updateReview(eq(1L), eq(1L), any(UpdateReviewRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/products/{productId}/reviews/{reviewId} - Should delete review")
    void testDeleteReview() throws Exception {
        doNothing().when(reviewService).deleteReview(1L, 1L);

        mockMvc.perform(delete("/api/products/1/reviews/1")).andExpect(status().isNoContent());

        verify(reviewService).deleteReview(1L, 1L);
    }

    // =================== QUESTION ENDPOINTS ===================

    @Test
    @DisplayName("GET /api/products/{id}/questions - Should return all questions")
    void testGetProductQuestions() throws Exception {
        List<QuestionDTO> questions = readJsonList("question/get-product-questions.json", QuestionDTO.class);

        when(questionService.getProductQuestions(1L)).thenReturn(questions);

        mockMvc.perform(get("/api/products/1/questions")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].question").value("What is the battery life?"));

        verify(questionService).getProductQuestions(1L);
    }

    @Test
    @DisplayName("POST /api/products/{id}/questions - Should create question")
    void testAddQuestion() throws Exception {
        QuestionDTO created = readJson("question/create-question-response.json", QuestionDTO.class);
        String requestBody = readJsonString("question/create-question-request.json");

        when(questionService.addQuestion(eq(1L), any(CreateQuestionRequest.class))).thenReturn(created);

        mockMvc.perform(post("/api/products/1/questions").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.question").value("Does it support 5G?"));

        verify(questionService).addQuestion(eq(1L), any(CreateQuestionRequest.class));
    }

    @Test
    @DisplayName("PUT /api/products/{productId}/questions/{questionId} - Should answer question")
    void testAnswerQuestion() throws Exception {
        QuestionDTO answered = readJson("question/answer-question-response.json", QuestionDTO.class);
        String requestBody = readJsonString("question/answer-question-request.json");

        when(questionService.answerQuestion(eq(1L), eq(1L), any(AnswerQuestionRequest.class))).thenReturn(answered);

        mockMvc.perform(put("/api/products/1/questions/1").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andExpect(jsonPath("$.answer").value("Yes, it supports 5G"));

        verify(questionService).answerQuestion(eq(1L), eq(1L), any(AnswerQuestionRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/products/{productId}/questions/{questionId} - Should delete question")
    void testDeleteQuestion() throws Exception {
        doNothing().when(questionService).deleteQuestion(1L, 1L);

        mockMvc.perform(delete("/api/products/1/questions/1")).andExpect(status().isNoContent());

        verify(questionService).deleteQuestion(1L, 1L);
    }

    @Test
    @DisplayName("GET /api/products - Should return empty list when no products")
    void testGetAllProducts_Empty() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));

        verify(productService).getAllProducts();
    }
}
