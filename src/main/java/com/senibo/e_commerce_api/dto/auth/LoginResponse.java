package com.senibo.e_commerce_api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User login response")
public record LoginResponse(
        @Schema(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,

        @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "User's first name", example = "John")
        String firstname,

        @Schema(description = "User's last name", example = "Doe")
        String lastname,

        @Schema(description = "User's username", example = "johndoe123")
        String username,

        @Schema(description = "User's email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "User's role", example =
                "CUSTOMER")
        String role
) {
}
