package com.senibo.e_commerce_api.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing the payment authorization URL.")
public record PaymentResponse(
        @Schema(description = "The URL the user should be redirected to to complete payment.")
        String authorizationUrl
) {
}
