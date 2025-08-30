package com.senibo.e_commerce_api.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// Separate DTO for stock-only updates
public record UpdateStockRequest(
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be non-negative")
    Integer stockQuantity
) {}
