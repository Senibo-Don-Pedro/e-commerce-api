package com.senibo.e_commerce_api.client.dto;

public record PaystackWebhookPayload(
        String event,
        Data data
) {

    public record Data(
            String status,
            String reference,
            Integer amount
    ) {
    }
}
