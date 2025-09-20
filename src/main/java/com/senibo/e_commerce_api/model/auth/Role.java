package com.senibo.e_commerce_api.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "Role",
        description = "User role in the system",
        allowableValues = {"CUSTOMER", "ADMIN"}  // ← Explicitly show the values
)
public enum Role {
    CUSTOMER,
    ADMIN
}
