package com.senibo.e_commerce_api.dto.category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String slug,
        String description,
        Boolean isActive,
        Integer sortOrder,
        Integer productCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
