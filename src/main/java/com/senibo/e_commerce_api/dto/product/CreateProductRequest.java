package com.senibo.e_commerce_api.dto.product;

import com.senibo.e_commerce_api.model.product.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Schema for creating a new product.")
public record CreateProductRequest(

        @Schema(description = "Product name", example = "Classic Leather Wallet")
        @NotBlank(message = "Name is required")
        @Size(min = 5, message = " Name must be at least 5 characters")
        String name,

        @Schema(description = "Product description",
                example = "A handcrafted wallet made from genuine leather.")
        @NotBlank(message = "Description is required")
        @Size(min = 20, message = " Description must be at least 20 characters")
        String description,

        @Schema(description = "Product price", example = "49.99")
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        BigDecimal price,

        @Schema(description = "Stock quantity", example = "150")
        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity cannot be negative")
        Integer stockQuantity,

        @Schema(description = "Product category", example = "ACCESSORIES")
        @NotNull(message = "Category is required")
        ProductCategory category,

        @Schema(description = "Product image URL",
                example = "https://example.com/images/wallet.jpg")
        String imageUrl
) {
}
