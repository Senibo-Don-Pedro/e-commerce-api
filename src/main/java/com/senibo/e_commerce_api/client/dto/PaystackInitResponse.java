package com.senibo.e_commerce_api.client.dto;

public record PaystackInitResponse(boolean status, String message, Data data) {

    public record Data(String authorization_url, String reference) {
    }
}
