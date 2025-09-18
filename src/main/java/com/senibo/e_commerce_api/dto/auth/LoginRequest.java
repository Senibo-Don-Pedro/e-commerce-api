package com.senibo.e_commerce_api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User login request")
public record LoginRequest(
        @NotBlank(message = "Identifier cannot be empty")
        @Schema(description = "Username or email address", example = "johndoe123")
        String identifier,

        @NotBlank(message = "Password cannot be empty")
        @Schema(description = "User's password", example = "SecurePassword123!")
        String password
) {
}
