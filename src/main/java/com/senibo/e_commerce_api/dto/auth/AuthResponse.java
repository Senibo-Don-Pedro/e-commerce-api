package com.senibo.e_commerce_api.dto.auth;

import java.util.List;

public record AuthResponse(String username,
                           String email,
                           String firstname,
                           String lastname,
                           String token,
                           List<String> roles) {
}
