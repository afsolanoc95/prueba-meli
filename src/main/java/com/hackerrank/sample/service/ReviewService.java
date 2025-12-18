package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.CreateReviewRequest;
import com.hackerrank.sample.dto.ReviewDTO;
import com.hackerrank.sample.dto.UpdateReviewRequest;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.mapper.ReviewMapper;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.model.Review;
import com.hackerrank.sample.repository.ProductRepository;
import com.hackerrank.sample.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository,
            ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional(readOnly = true)
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "reviewCB")
    public List<ReviewDTO> getProductReviews(Long productId) {
        log.debug("Fetching reviews for product id: {}", productId);
        if (!productRepository.existsById(productId)) {
            throw new NoSuchResourceFoundException("Product not found with id: " + productId);
        }

        List<Review> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
        return reviews.stream().map(reviewMapper::toDTO).toList();
    }

    @Transactional
    public ReviewDTO addReview(Long productId, CreateReviewRequest request) {
        log.debug("Adding review for product id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchResourceFoundException("Product not found with id: " + productId));

        Review review = Review.builder().rating(request.getRating()).comment(request.getComment())
                .userName(request.getUserName()).product(product).build();
        review.onCreate(); // Helper to set timestamp if needed, but Builder might skip PrePersist logic if
                           // not managed.
                           // Actually JPA PrePersist works on entity lifecycle.
                           // But I should instantiate it correctly.
                           // Note: Builders don't run constructors, so field defaults in Lombok
                           // @Builder.Default are important.
                           // I added @Builder.Default to lists in Product, but let's check Review
                           // timestamp.

        Review savedReview = reviewRepository.save(review);
        log.info("Review added for product id: {}", productId);
        return reviewMapper.toDTO(savedReview);
    }

    @Transactional
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "reviewCB")
    public ReviewDTO updateReview(Long productId, Long reviewId, UpdateReviewRequest request) {
        log.debug("Updating review id: {} for product id: {}", reviewId, productId);
        if (!productRepository.existsById(productId)) {
            throw new NoSuchResourceFoundException("Product not found with id: " + productId);
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchResourceFoundException("Review not found with id: " + reviewId));

        if (!review.getProduct().getId().equals(productId)) {
            throw new NoSuchResourceFoundException("Review " + reviewId + " does not belong to product " + productId);
        }

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        if (request.getComment() != null) {
            review.setComment(request.getComment());
        }

        Review updatedReview = reviewRepository.save(review);
        log.info("Review updated, id: {}", reviewId);
        return reviewMapper.toDTO(updatedReview);
    }

    @Transactional
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "reviewCB")
    public void deleteReview(Long productId, Long reviewId) {
        log.debug("Deleting review id: {} for product id: {}", reviewId, productId);
        if (!productRepository.existsById(productId)) {
            throw new NoSuchResourceFoundException("Product not found with id: " + productId);
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchResourceFoundException("Review not found with id: " + reviewId));

        if (!review.getProduct().getId().equals(productId)) {
            throw new NoSuchResourceFoundException("Review " + reviewId + " does not belong to product " + productId);
        }

        reviewRepository.deleteById(reviewId);
        log.info("Review deleted, id: {}", reviewId);
    }
}
