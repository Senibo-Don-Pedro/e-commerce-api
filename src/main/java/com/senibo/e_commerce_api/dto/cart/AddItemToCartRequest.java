package com.senibo.e_commerce_api.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Request to add an item to the shopping cart.")
public record AddItemToCartRequest(
        @Schema(description = "The unique ID of the product to add.",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Product ID cannot be null.")
        UUID productId,

        @Schema(description = "The quantity of the product to add.", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Quantity cannot be null.")
        @Min(value = 1, message = "Quantity must be at least 1.")
        Integer quantity
) {
}
