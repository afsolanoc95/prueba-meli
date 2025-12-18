package com.hackerrank.sample.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String currency;
    private Integer discount;
    private Integer availableQuantity;
    private Integer soldQuantity;
    private String condition;
    private String description;
    private String warranty;
    private LocalDateTime createdAt;

    private List<String> images;
    private List<AttributeDTO> attributes;
    private SellerDTO seller;
    private ReviewSummaryDTO reviewSummary;
    private List<ReviewDTO> recentReviews;
    private List<QuestionDTO> questions;
}
