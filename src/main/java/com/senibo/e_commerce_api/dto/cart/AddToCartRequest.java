package com.senibo.e_commerce_api.dto.cart;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record AddToCartRequest(
        @NotNull(message = "Product Id is Required")
        UUID productId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Max(value = 100, message = "Quantity cannot exceed 100")
        Integer quantity
) {
}
