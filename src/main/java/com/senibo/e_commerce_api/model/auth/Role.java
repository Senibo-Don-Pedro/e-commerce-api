package com.senibo.e_commerce_api.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Role", description = "User role in the system")
public enum Role {
    CUSTOMER,
    ADMIN
}
