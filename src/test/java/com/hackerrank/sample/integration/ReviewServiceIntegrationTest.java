package com.hackerrank.sample.integration;

import com.hackerrank.sample.dto.CreateReviewRequest;
import com.hackerrank.sample.dto.ReviewDTO;
import com.hackerrank.sample.dto.UpdateReviewRequest;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import com.hackerrank.sample.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for ReviewService covering complete review lifecycle
 */
@Transactional
class ReviewServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUpProductForReviews() {
        testProduct = createBasicProductBuilder().build();
        testProduct = productRepository.save(testProduct);
    }

    @Test
    @DisplayName("Should complete review lifecycle: create -> get -> update -> delete")
    void testCompleteReviewLifecycle() throws IOException {
        // 1. CREATE: Add multiple reviews
        CreateReviewRequest review1 = readJson("review/create-review-request.json", CreateReviewRequest.class);
        review1.setComment("Excellent product!");
        review1.setRating(5);

        CreateReviewRequest review2 = readJson("review/create-review-request.json", CreateReviewRequest.class);
        review2.setRating(4);
        review2.setComment("Good quality");
        review2.setUserName("user2");

        ReviewDTO created1 = reviewService.addReview(testProduct.getId(), review1);
        ReviewDTO created2 = reviewService.addReview(testProduct.getId(), review2);

        assertThat(created1).isNotNull();
        assertThat(created1.getRating()).isEqualTo(5);
        assertThat(created1.getComment()).isEqualTo("Excellent product!");
        assertThat(created1.getCreatedAt()).isNotNull();

        // 2. GET ALL: Retrieve all reviews for product
        List<ReviewDTO> reviews = reviewService.getProductReviews(testProduct.getId());

        assertThat(reviews).hasSize(2);
        assertThat(reviews).extracting(ReviewDTO::getRating).contains(5, 4);

        // 3. UPDATE: Update a review
        UpdateReviewRequest updateRequest = readJson("review/update-review-request.json", UpdateReviewRequest.class);
        updateRequest.setRating(5);
        updateRequest.setComment("Updated: Still excellent!");

        ReviewDTO updated = reviewService.updateReview(testProduct.getId(), created2.getId(), updateRequest);

        assertThat(updated.getRating()).isEqualTo(5);
        assertThat(updated.getComment()).isEqualTo("Updated: Still excellent!");

        // 4. DELETE: Delete a review
        reviewService.deleteReview(testProduct.getId(), created1.getId());

        // 5. VERIFY: Confirm only one review remains
        List<ReviewDTO> remainingReviews = reviewService.getProductReviews(testProduct.getId());
        assertThat(remainingReviews).hasSize(1);
        assertThat(remainingReviews.get(0).getId()).isEqualTo(created2.getId());
    }

    @Test
    @DisplayName("Should throw exception when adding review to non-existent product")
    void testAddReviewToNonExistentProduct_ShouldThrowException() throws IOException {
        CreateReviewRequest request = readJson("review/create-review-request.json", CreateReviewRequest.class);

        assertThatThrownBy(() -> reviewService.addReview(99999L, request))
                .isInstanceOf(NoSuchResourceFoundException.class).hasMessageContaining("Product not found");
    }

    @Test
    @DisplayName("Should throw exception when updating review from different product")
    void testUpdateReviewFromDifferentProduct_ShouldThrowException() throws IOException {
        // Create two products
        final Product product2 = productRepository.save(createBasicProductBuilder().title("Product 2").build());

        // Add review to product 1
        CreateReviewRequest request = readJson("review/create-review-request.json", CreateReviewRequest.class);
        ReviewDTO review = reviewService.addReview(testProduct.getId(), request);

        // Try to update review from product 2's endpoint
        UpdateReviewRequest updateRequest = readJson("review/update-review-request.json", UpdateReviewRequest.class);

        Long product2Id = product2.getId();
        Long reviewId = review.getId();

        assertThatThrownBy(() -> reviewService.updateReview(product2Id, reviewId, updateRequest))
                .isInstanceOf(NoSuchResourceFoundException.class).hasMessageContaining("does not belong to product");
    }

    @Test
    @DisplayName("Should throw exception when deleting review from different product")
    void testDeleteReviewFromDifferentProduct_ShouldThrowException() throws IOException {
        // Create second product
        final Product product2 = productRepository.save(createBasicProductBuilder().title("Product 2").build());

        // Add review to product 1
        CreateReviewRequest request = readJson("review/create-review-request.json", CreateReviewRequest.class);
        ReviewDTO review = reviewService.addReview(testProduct.getId(), request);

        // Try to delete from product 2's endpoint
        Long product2Id = product2.getId();
        Long reviewId = review.getId();
        assertThatThrownBy(() -> reviewService.deleteReview(product2Id, reviewId))
                .isInstanceOf(NoSuchResourceFoundException.class).hasMessageContaining("does not belong to product");
    }

    @Test
    @DisplayName("Should return reviews ordered by created date descending")
    void testReviewsOrderedByCreatedAtDesc() throws IOException {
        // Add multiple reviews
        CreateReviewRequest request1 = readJson("review/create-review-request.json", CreateReviewRequest.class);
        request1.setComment("First review");
        reviewService.addReview(testProduct.getId(), request1);

        CreateReviewRequest request2 = readJson("review/create-review-request.json", CreateReviewRequest.class);
        request2.setComment("Second review");
        request2.setUserName("user2");
        reviewService.addReview(testProduct.getId(), request2);

        CreateReviewRequest request3 = readJson("review/create-review-request.json", CreateReviewRequest.class);
        request3.setComment("Third review");
        request3.setUserName("user3");
        reviewService.addReview(testProduct.getId(), request3);

        // Get reviews
        List<ReviewDTO> reviews = reviewService.getProductReviews(testProduct.getId());

        assertThat(reviews).hasSize(3);
        // Verify we have all comments
        assertThat(reviews).extracting(ReviewDTO::getComment).containsExactlyInAnyOrder("Third review", "Second review",
                "First review");
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent review")
    void testUpdateNonExistentReview_ShouldThrowException() throws IOException {
        UpdateReviewRequest request = readJson("review/update-review-request.json", UpdateReviewRequest.class);

        Long productId = testProduct.getId();
        assertThatThrownBy(() -> reviewService.updateReview(productId, 99999L, request))
                .isInstanceOf(NoSuchResourceFoundException.class).hasMessageContaining("Review not found");
    }
}
