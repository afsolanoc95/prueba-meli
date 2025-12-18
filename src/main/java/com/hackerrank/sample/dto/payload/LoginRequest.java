package com.hackerrank.sample.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(example = "john_doe")
    @NotBlank
    private String username;

    @Schema(example = "password123")
    @NotBlank
    private String password;
}
