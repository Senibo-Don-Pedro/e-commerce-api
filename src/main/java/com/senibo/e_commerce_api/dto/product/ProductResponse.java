package com.senibo.e_commerce_api.dto.product;

import com.senibo.e_commerce_api.model.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Product response")
public record ProductResponse(

        @Schema(description = "Product ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Product name", example = "iPhone 15")
        String name,

        @Schema(description = "Product description",
                example = "Latest iPhone model with advanced features")
        String description,

        @Schema(description = "Product price", example = "999.99")
        BigDecimal price,

        @Schema(description = "Available stock quantity", example = "50")
        Integer stockQuantity,

        @Schema(description = "Product category", example = "ELECTRONICS")
        String category,
        // Display name from enum

        @Schema(description = "Product image URL",
                example = "https://example.com/iphone.jpg")
        String imageUrl,

        @Schema(description = "Product SKU", example = "ELEC-IPHONE15-001")
        String sku

) {
    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory().toString(), // Assumes category is an enum on Product
                product.getImageUrl(),
                product.getSku()
        );
    }
}
