package com.senibo.e_commerce_api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 6, max = 30, message = "Username must be between 6 and 30 characters long")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email is invalid")
//        @Pattern(
//                regexp = "^[A-Za-z0-9._%+-]+@example\\.com$",
//                message = "Email must be in the example.com domain"
//        )
        String email,

        @NotBlank(message = "Firstname is required")
        String firstname,

        @NotBlank(message = "Lastname is required")
        String lastname,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                // at least one digit, one lower, one upper, one special
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain at least one digit, one lower, one upper, one special character"
        )
        String password
) {
}
