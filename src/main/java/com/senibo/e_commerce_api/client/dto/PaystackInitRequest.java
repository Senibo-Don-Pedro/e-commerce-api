package com.senibo.e_commerce_api.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Payload sent to Paystack to initialize a payment transaction.")
public record PaystackInitRequest(
        @Schema(description = "Customer's email address.", example = "customer@example.com")
        String email,

        @Schema(description = "Transaction amount in the smallest currency unit (e.g., kobo).",
                example = "500000")
        Integer amount,

        @Schema(description = "Unique reference for this transaction.",
                example = "unique-transaction-ref-123")
        String reference
) {
}
