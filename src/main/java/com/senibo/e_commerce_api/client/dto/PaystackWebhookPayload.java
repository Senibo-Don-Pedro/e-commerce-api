package com.senibo.e_commerce_api.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Payload received from a Paystack webhook event.")
public record PaystackWebhookPayload(
        @Schema(description = "The type of event that occurred.", example = "charge.success")
        String event,

        @Schema(description = "A nested object containing details about the transaction event.")
        Data data
) {

    @Schema(description = "Details of the transaction event.")
    public record Data(
            @Schema(description = "The status of the transaction.", example = "success")
            String status,

            @Schema(description = "The unique reference for the transaction.",
                    example = "unique-transaction-ref-123")
            String reference,

            @Schema(description = "The transaction amount in the smallest currency unit.",
                    example = "500000")
            Integer amount
    ) {
    }
}
