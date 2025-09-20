package com.senibo.e_commerce_api.dto.product;

import com.senibo.e_commerce_api.model.product.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Schema for replacing a product. All fields except imageUrl are required.")
public record UpdateProductRequest(

        @Schema(description = "Full new name of the product.", example = "Premium Leather Wallet")
        @NotBlank // <-- Add this
        @Size(min = 5, message = "Name must be at least 5 characters")
        String name,

        @Schema(description = "Complete new description for the product.",
                example = "An upgraded wallet made from premium full-grain leather.")
        @NotBlank // <-- Add this
        @Size(min = 20, message = "Description must be at least 20 characters")
        String description,

        @Schema(description = "New price for the product.", example = "59.99")
        @NotNull // <-- Add this
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        BigDecimal price,

        @Schema(description = "Updated stock quantity.", example = "120")
        @NotNull // <-- Add this
        @Min(value = 0, message = "Stock quantity cannot be negative")
        Integer stockQuantity,

        @Schema(description = "New product category.", example = "ELECTRONICS")
        @NotNull // <-- Add this
        ProductCategory category,

        @Schema(description = "New product image URL (optional).",
                example = "https://example.com/images/premium-wallet.jpg")
        String imageUrl // <-- This remains optional
) {
}
