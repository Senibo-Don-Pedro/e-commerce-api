package com.senibo.e_commerce_api.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Simple utility to create Pageable objects with basic validation
 * Keeps the service clean and easy to understand
 */
@Component
public class PaginationValidator {

    /**
     * Creates a Pageable object with simple validation
     */
    public Pageable createPageable(int page, int pageSize, String sortBy, String sortDirection) {

        // Simple validation - just the basics
        int validPage = Math.max(0, page);                    // No negative pages
        int validPageSize = Math.min(Math.max(1, pageSize), 100);  // Between 1-100

        // Simple sort direction check
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // Create sort (if sortBy is invalid, Spring will handle the error)
        Sort sort = Sort.by(direction, sortBy);

        // Return the pageable object
        return PageRequest.of(validPage, validPageSize, sort);
    }
}
