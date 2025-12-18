package com.hackerrank.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequest {

    @Schema(example = "Is this product compatible with Windows 11?")
    @NotBlank(message = "Question is required")
    @Size(max = 1000, message = "Question must not exceed 1000 characters")
    private String question;

    @Schema(example = "tech_guru")
    @NotBlank(message = "User name is required")
    @Size(max = 200, message = "User name must not exceed 200 characters")
    private String userName;
}
