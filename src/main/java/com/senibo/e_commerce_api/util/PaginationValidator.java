package com.senibo.e_commerce_api.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * A utility component to create {@link Pageable} objects for database queries.
 * <p>
 * This class centralizes pagination logic, applies basic validation to page and size parameters,
 * and constructs a Pageable object, keeping the service layer clean and focused on business logic.
 */
@Component
public class PaginationValidator {

    /**
     * Creates a {@link Pageable} object from raw pagination and sorting parameters.
     * <p>
     * It validates the page number to be non-negative and clamps the page size
     * between 1 and 100 to prevent excessive data retrieval.
     *
     * @param page          The requested page number (0-indexed).
     * @param pageSize      The requested number of items per page.
     * @param sortBy        The property to sort the results by.
     * @param sortDirection The direction of the sort ("asc" or "desc").
     * @return A configured {@link Pageable} instance.
     */
    public Pageable createPageable(int page, int pageSize, String sortBy, String sortDirection) {
        // Ensure page number is not negative
        int validPage = Math.max(0, page);
        // Clamp page size to be between 1 and 100
        int validPageSize = Math.min(Math.max(1, pageSize), 100);

        // Determine sort direction, defaulting to ASC
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(validPage, validPageSize, sort);
    }
}
