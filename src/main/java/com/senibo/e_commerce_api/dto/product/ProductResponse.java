package com.senibo.e_commerce_api.dto.product;

import com.senibo.e_commerce_api.dto.category.CategoryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// Response DTOs
public record ProductResponse(
    UUID id,
    String sku,
    String name,
    String description,
    BigDecimal price,
    Integer stockQuantity,
    Integer reservedQuantity,
    Integer availableStock,
    String imageUrl,
    Boolean isActive,
    Double weight,
    CategoryResponse category,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
