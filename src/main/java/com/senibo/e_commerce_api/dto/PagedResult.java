package com.senibo.e_commerce_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Clean pagination response that matches your senior's C# approach
 * This creates a simple, easy-to-understand JSON response
 */
@Schema(description = "Paged result containing items and pagination info")
public record PagedResult<T>(

        @Schema(description = "List of items for current page")
        @JsonProperty("items")
        List<T> items,

        @Schema(description = "Current page number (0-based)")
        @JsonProperty("page")
        int page,

        @Schema(description = "Number of items per page")
        @JsonProperty("page_size")
        int pageSize,

        @Schema(description = "Total number of items across all pages")
        @JsonProperty("total_count")
        long totalCount,

        @Schema(description = "Total number of pages")
        @JsonProperty("total_pages")
        int totalPages,

        @Schema(description = "Whether there is a next page available")
        @JsonProperty("has_next")
        boolean hasNext,

        @Schema(description = "Whether there is a previous page available")
        @JsonProperty("has_prev")
        boolean hasPrev

) {

    /**
     * Converts Spring's Page object to our clean PagedResult
     * This hides all the complex Spring internals from the API response
     */
    public static <T> PagedResult<T> from(Page<T> page) {
        return new PagedResult<>(
                page.getContent(),           // The actual items
                page.getNumber(),            // Current page number
                page.getSize(),              // Items per page
                page.getTotalElements(),     // Total items across all pages
                page.getTotalPages(),        // Total number of pages
                page.hasNext(),              // Is there a next page?
                page.hasPrevious()           // Is there a previous page?
        );
    }

}
