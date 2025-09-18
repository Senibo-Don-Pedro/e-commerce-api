package com.senibo.e_commerce_api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User registration request")
public record RegisterRequest(
        @NotBlank(message = "First name cannot be empty")
        @Schema(description = "User's first name", example = "John")
        String firstname,

        @NotBlank(message = "Last name cannot be empty")
        @Schema(description = "User's last name", example = "Doe")
        String lastname,

        @NotBlank(message = "Username cannot be empty")
        @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
        @Schema(description = "Unique username", example = "johndoe123")
        String username,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Please provide a valid email address")
        @Schema(description = "User's email address", example = "john.doe@example.com")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Schema(description = "User's password", example = "SecurePassword123!")
        String password
) {
}
