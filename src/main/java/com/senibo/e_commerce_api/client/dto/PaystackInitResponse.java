package com.senibo.e_commerce_api.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Response received from Paystack after initiating a transaction.")
public record PaystackInitResponse(
        @Schema(description = "Indicates if the API call was successful.", example = "true")
        boolean status,

        @Schema(description = "A descriptive message from the API.",
                example = "Authorization URL created")
        String message,

        @Schema(description = "The nested object containing authorization details.")
        Data data
) {

    @Schema(description = "Contains the authorization details for the transaction.")
    public record Data(
            @Schema(description = "The URL the user should be redirected to to complete payment.",
                    example = "https://checkout.paystack.com/h9vkl0d92nd")
            String authorization_url,

            @Schema(description = "The unique reference for the transaction.",
                    example = "unique-transaction-ref-123")
            String reference
    ) {
    }
}
