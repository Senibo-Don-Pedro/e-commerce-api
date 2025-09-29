package com.senibo.e_commerce_api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for user profile information")
public record UserDTO(
        String id,
        String firstname,
        String lastname,
        String username,
        String email,
        String role

) {
}
