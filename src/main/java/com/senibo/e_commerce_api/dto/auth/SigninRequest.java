package com.senibo.e_commerce_api.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record SigninRequest(
        @NotBlank(message = "User name or email is required")
        String identifier,

        @NotBlank(message = "Password is required")
        String password
) {
}
