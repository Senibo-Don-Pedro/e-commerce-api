package com.senibo.e_commerce_api.dto.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Category name is required")
        @Size(max = 100, message = "Name must be less than 100 characters")
        String name,

        @Size(max = 500, message = "Description must be less than 500 characters")
        String description,

        @Min(value = 0, message = "Sort order must be non-negative")
        Integer sortOrder,

        Boolean isActive  // Only used for updates, ignored for creates
) {}
