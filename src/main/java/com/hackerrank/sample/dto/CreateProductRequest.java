package com.hackerrank.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @Schema(example = "iPhone 15 Pro Max")
    @NotBlank(message = "Title is required")
    @Size(max = 500, message = "Title must not exceed 500 characters")
    private String title;

    @Schema(example = "1299.99")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @Schema(example = "1499.99")
    @DecimalMin(value = "0.01", message = "Original price must be greater than 0")
    private BigDecimal originalPrice;

    @Schema(example = "USD")
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters (e.g., USD)")
    private String currency;

    @Schema(example = "50")
    @NotNull(message = "Available quantity is required")
    @Min(value = 0, message = "Available quantity cannot be negative")
    private Integer availableQuantity;

    @Schema(example = "new")
    @NotBlank(message = "Condition is required")
    @Pattern(regexp = "new|used|refurbished", message = "Condition must be: new, used, or refurbished")
    private String condition;

    @Schema(example = "The latest iPhone with titanium design.")
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(example = "1 year Apple warranty")
    private String warranty;

    @Schema(example = "12345")
    @NotNull(message = "Seller ID is required")
    private Long sellerId;

    @Schema(example = "[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]")
    private List<String> imageUrls;

    private List<AttributeRequest> attributes;

    // Nested class for attributes
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeRequest {
        @Schema(example = "Color")
        @NotBlank(message = "Attribute name is required")
        private String name;

        @Schema(example = "Titanium Black")
        @NotBlank(message = "Attribute value is required")
        private String value;
    }
}
