package com.hackerrank.sample.mapper;

import com.hackerrank.sample.dto.*;
import com.hackerrank.sample.model.*;
import com.hackerrank.sample.repository.ReviewRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

        private final ReviewRepository reviewRepository;
        private final ReviewMapper reviewMapper;
        private final QuestionMapper questionMapper;
        private final SellerMapper sellerMapper;

        public ProductMapper(ReviewRepository reviewRepository, ReviewMapper reviewMapper,
                        QuestionMapper questionMapper, SellerMapper sellerMapper) {
                this.reviewRepository = reviewRepository;
                this.reviewMapper = reviewMapper;
                this.questionMapper = questionMapper;
                this.sellerMapper = sellerMapper;
        }

        public ProductSummaryDTO toSummaryDTO(Product product) {
                String thumbnail = product.getImages().stream().filter(ProductImage::getIsPrimary).findFirst()
                                .map(ProductImage::getUrl).orElseGet(() -> product.getImages().isEmpty() ? null
                                                : product.getImages().get(0).getUrl());

                Double avgRating = reviewRepository.findAverageRatingByProductId(product.getId());

                return ProductSummaryDTO.builder().id(product.getId()).title(product.getTitle())
                                .price(product.getPrice()).currency(product.getCurrency())
                                .condition(product.getCondition()).availableQuantity(product.getAvailableQuantity())
                                .soldQuantity(product.getSoldQuantity()).thumbnail(thumbnail)
                                .averageRating(avgRating != null ? avgRating : 0.0).build();
        }

        public ProductDetailDTO toDetailDTO(Product product) {
                int discount = 0;
                if (product.getOriginalPrice() != null && product.getOriginalPrice().compareTo(BigDecimal.ZERO) > 0) {
                        discount = product.getOriginalPrice().subtract(product.getPrice())
                                        .divide(product.getOriginalPrice(), 4, RoundingMode.HALF_UP)
                                        .multiply(new BigDecimal("100")).intValue();
                }

                List<String> imageUrls = product.getImages().stream().map(ProductImage::getUrl)
                                .collect(Collectors.toList());

                List<AttributeDTO> attributes = product.getAttributes().stream()
                                .map(attr -> new AttributeDTO(attr.getName(), attr.getAttributeValue()))
                                .collect(Collectors.toList());

                List<ReviewDTO> recentReviews = product.getReviews().stream()
                                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt())).limit(5)
                                .map(reviewMapper::toDTO).collect(Collectors.toList());

                List<QuestionDTO> questions = product.getQuestions().stream()
                                .sorted((q1, q2) -> q2.getCreatedAt().compareTo(q1.getCreatedAt())).limit(10)
                                .map(questionMapper::toDTO).collect(Collectors.toList());

                return ProductDetailDTO.builder().id(product.getId()).title(product.getTitle())
                                .price(product.getPrice()).originalPrice(product.getOriginalPrice())
                                .currency(product.getCurrency()).availableQuantity(product.getAvailableQuantity())
                                .soldQuantity(product.getSoldQuantity()).condition(product.getCondition())
                                .description(product.getDescription()).warranty(product.getWarranty())
                                .createdAt(product.getCreatedAt()).discount(discount).images(imageUrls)
                                .attributes(attributes).seller(sellerMapper.toDTO(product.getSeller()))
                                .reviewSummary(getReviewSummary(product.getId())).recentReviews(recentReviews)
                                .questions(questions).build();
        }

        private ReviewSummaryDTO getReviewSummary(Long productId) {
                Double avgRating = reviewRepository.findAverageRatingByProductId(productId);
                List<Review> allReviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);

                return ReviewSummaryDTO.builder()
                                .averageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0)
                                .totalReviews(allReviews.size())
                                .fiveStars(reviewRepository.countByProductIdAndRating(productId, 5))
                                .fourStars(reviewRepository.countByProductIdAndRating(productId, 4))
                                .threeStars(reviewRepository.countByProductIdAndRating(productId, 3))
                                .twoStars(reviewRepository.countByProductIdAndRating(productId, 2))
                                .oneStar(reviewRepository.countByProductIdAndRating(productId, 1)).build();
        }
}
