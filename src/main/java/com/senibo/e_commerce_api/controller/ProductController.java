package com.senibo.e_commerce_api.controller;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.PagedResult;
import com.senibo.e_commerce_api.dto.product.ProductResponse;
import com.senibo.e_commerce_api.model.product.ProductCategory;
import com.senibo.e_commerce_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "product-controller", description = "Endpoints for viewing and searching products.")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products with filtering, pagination, and sorting")
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<PagedResult<ProductResponse>>> getAllProducts(
            // Pagination Parameters
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(description = "Number of items per page (max 100)", example = "10")
            @RequestParam(defaultValue = "10")
            int pageSize,

            // Sorting Parameters
            @Parameter(description = "Field to sort by",
                    schema = @Schema(allowableValues = {"name", "price", "createdAt"}))
            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @Parameter(description = "Sort direction",
                    schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "desc")
            String sortDirection,

            // --- NEW: Filtering Parameters ---

            @Parameter(description = "Search by product name or SKU")
            @RequestParam(required = false)
            String searchTerm,

            @Parameter(description = "Filter by category")
            @RequestParam(required = false)
            ProductCategory category,

            @Parameter(description = "Filter by minimum price (inclusive)", example = "50.00")
            @RequestParam(required = false)
            BigDecimal minPrice,

            @Parameter(description = "Filter by maximum price (inclusive)", example = "250.00")
            @RequestParam(required = false)
            BigDecimal maxPrice
    ) {
        // Updated call to the service with all parameters
        var response = productService.findAllProducts(
                page, pageSize, sortBy, sortDirection,
                Optional.ofNullable(searchTerm),
                Optional.ofNullable(category),
                Optional.ofNullable(minPrice),
                Optional.ofNullable(maxPrice)
        );

        return ResponseEntity.ok(response);
    }
}
