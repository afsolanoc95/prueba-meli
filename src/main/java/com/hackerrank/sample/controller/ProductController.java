package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.ProductDetailDTO;
import com.hackerrank.sample.dto.ProductSummaryDTO;
import com.hackerrank.sample.dto.QuestionDTO;
import com.hackerrank.sample.dto.ReviewDTO;
import com.hackerrank.sample.dto.CreateProductRequest;
import com.hackerrank.sample.dto.CreateReviewRequest;
import com.hackerrank.sample.dto.CreateQuestionRequest;
import com.hackerrank.sample.dto.UpdateProductRequest;
import com.hackerrank.sample.dto.UpdateReviewRequest;
import com.hackerrank.sample.dto.AnswerQuestionRequest;
import com.hackerrank.sample.service.ProductService;
import com.hackerrank.sample.service.ReviewService;
import com.hackerrank.sample.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final QuestionService questionService;

    public ProductController(ProductService productService, ReviewService reviewService,
            QuestionService questionService) {
        this.productService = productService;
        this.reviewService = reviewService;
        this.questionService = questionService;
    }

    /**
     * Get all products (summary view) GET /api/products
     */
    @Operation(summary = "Listar todos los productos", description = "Obtiene una lista resumida de todos los productos disponibles.")
    @GetMapping
    public ResponseEntity<List<ProductSummaryDTO>> getAllProducts() {
        List<ProductSummaryDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Get product details by ID GET /api/products/{id}
     */
    @Operation(summary = "Obtener producto por ID", description = "Obtiene los detalles completos de un producto específico.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductById(@PathVariable Long id) {
        ProductDetailDTO product = productService.getProductDetail(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Get all reviews for a product GET /api/products/{id}/reviews
     */
    @Operation(summary = "Obtener reseñas de un producto", description = "Obtiene todas las reseñas de un producto específico.")
    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> getProductReviews(@PathVariable Long id) {
        List<ReviewDTO> reviews = reviewService.getProductReviews(id);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get all questions for a product GET /api/products/{id}/questions
     */
    @Operation(summary = "Obtener preguntas de un producto", description = "Obtiene todas las preguntas realizadas sobre un producto.")
    @GetMapping("/{id}/questions")
    public ResponseEntity<List<QuestionDTO>> getProductQuestions(@PathVariable Long id) {
        List<QuestionDTO> questions = questionService.getProductQuestions(id);
        return ResponseEntity.ok(questions);
    }

    /**
     * Create a new product POST /api/products
     */
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nevo producto con todos sus detalles y atributos.")
    @PostMapping
    public ResponseEntity<ProductDetailDTO> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductDetailDTO product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    /**
     * Add a review to a product POST /api/products/{id}/reviews
     */
    @Operation(summary = "Agregar una reseña", description = "Agrega una nueva reseña a un producto existente.")
    @PostMapping("/{id}/reviews")
    public ResponseEntity<ReviewDTO> addReview(@PathVariable Long id, @Valid @RequestBody CreateReviewRequest request) {
        ReviewDTO review = reviewService.addReview(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    /**
     * Add a question to a product POST /api/products/{id}/questions
     */
    @Operation(summary = "Agregar una pregunta", description = "Agrega una nueva pregunta a un producto existente.")
    @PostMapping("/{id}/questions")
    public ResponseEntity<QuestionDTO> addQuestion(@PathVariable Long id,
            @Valid @RequestBody CreateQuestionRequest request) {
        QuestionDTO question = questionService.addQuestion(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }

    /**
     * Update a product PUT /api/products/{id}
     */
    @Operation(summary = "Actualizar un producto", description = "Actualiza los detalles de un producto existente.")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> updateProduct(@PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        ProductDetailDTO product = productService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }

    /**
     * Delete a product DELETE /api/products/{id}
     */
    @Operation(summary = "Eliminar un producto", description = "Elimina un producto del sistema por su ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update a review PUT /api/products/{productId}/reviews/{reviewId}
     */
    @Operation(summary = "Actualizar una reseña", description = "Actualiza el contenido o calificación de una reseña existente.")
    @PutMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long productId, @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewRequest request) {
        ReviewDTO review = reviewService.updateReview(productId, reviewId, request);
        return ResponseEntity.ok(review);
    }

    /**
     * Delete a review DELETE /api/products/{productId}/reviews/{reviewId}
     */
    @Operation(summary = "Eliminar una reseña", description = "Elimina una reseña específica de un producto.")
    @DeleteMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long productId, @PathVariable Long reviewId) {
        reviewService.deleteReview(productId, reviewId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Answer a question PUT /api/products/{productId}/questions/{questionId}
     */
    @Operation(summary = "Responder una pregunta", description = "Proporciona una respuesta a una pregunta de un producto.")
    @PutMapping("/{productId}/questions/{questionId}")
    public ResponseEntity<QuestionDTO> answerQuestion(@PathVariable Long productId, @PathVariable Long questionId,
            @Valid @RequestBody AnswerQuestionRequest request) {
        QuestionDTO question = questionService.answerQuestion(productId, questionId, request);
        return ResponseEntity.ok(question);
    }

    /**
     * Delete a question DELETE /api/products/{productId}/questions/{questionId}
     */
    @Operation(summary = "Eliminar una pregunta", description = "Elimina una pregunta específica de un producto.")
    @DeleteMapping("/{productId}/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long productId, @PathVariable Long questionId) {
        questionService.deleteQuestion(productId, questionId);
        return ResponseEntity.noContent().build();
    }
}
