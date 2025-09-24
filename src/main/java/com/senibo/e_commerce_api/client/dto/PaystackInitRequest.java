package com.senibo.e_commerce_api.client.dto;

public record PaystackInitRequest(
        String email,
        Integer amount,
        String reference
) {
}
