package com.senibo.e_commerce_api.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(
    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Name must be less than 200 characters")
    String name,
    
    String sku, // Optional - auto-generate if null for creates
    
    @Size(max = 2000, message = "Description must be less than 2000 characters")
    String description,
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Invalid price format") BigDecimal price,
    
    @NotNull(message = "Category is required") UUID categoryId,
    
    @Min(value = 0, message = "Stock quantity must be non-negative")
    Integer stockQuantity,
    
    String imageUrl,
    
    @Min(value = 0, message = "Weight must be non-negative")
    Double weight,
    
    Boolean isActive  // Only used for updates
) {}
