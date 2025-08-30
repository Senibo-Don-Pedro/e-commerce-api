package com.senibo.e_commerce_api.dto.product;

import java.math.BigDecimal;
import java.util.UUID;

// Search/filter DTO
public record ProductSearchRequest(
        String name,        // Search by name
        UUID categoryId,    // Filter by category
        BigDecimal minPrice, // Price range filtering
        BigDecimal maxPrice,
        Boolean inStock     // Only show products in stock
) {}
