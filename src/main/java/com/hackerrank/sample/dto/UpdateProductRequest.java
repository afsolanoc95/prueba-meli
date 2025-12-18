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
public class UpdateProductRequest {

    @Schema(example = "iPhone 15 Pro Max (Titanium Blue)")
    @Size(max = 500, message = "Title must not exceed 500 characters")
    private String title;

    @Schema(example = "1199.99")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @Schema(example = "1299.99")
    @DecimalMin(value = "0.01", message = "Original price must be greater than 0")
    private BigDecimal originalPrice;

    @Schema(example = "45")
    @Min(value = 0, message = "Available quantity cannot be negative")
    private Integer availableQuantity;

    @Schema(example = "Updated description including new features.")
    private String description;

    @Schema(example = "2 years extended warranty")
    private String warranty;

    @Schema(example = "[\"http://example.com/new_image.jpg\"]")
    private List<String> imageUrls;

    private List<AttributeRequest> attributes;

    // Nested class for attributes
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeRequest {
        @Schema(example = "Storage")
        @NotBlank(message = "Attribute name is required")
        private String name;

        @Schema(example = "512GB")
        @NotBlank(message = "Attribute value is required")
        private String value;
    }
}
