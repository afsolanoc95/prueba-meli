package com.hackerrank.sample.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private String currency;
    private String thumbnail;
    private String condition;
    private Integer availableQuantity;
    private Integer soldQuantity;
    private Double averageRating;
}
