package com.hackerrank.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerQuestionRequest {

    @Schema(example = "Yes, it is fully compatible with Windows 11.")
    @NotBlank(message = "Answer is required")
    @Size(max = 2000, message = "Answer must not exceed 2000 characters")
    private String answer;
}
