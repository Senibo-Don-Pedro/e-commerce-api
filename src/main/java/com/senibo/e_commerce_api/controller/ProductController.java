package com.senibo.e_commerce_api.controller;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.PagedResult;
import com.senibo.e_commerce_api.dto.product.ProductResponse;
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

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "product-controller", description = "Endpoints for products")
public class ProductController {

    public final ProductService productService;


    @Operation(summary = "Gets all products",
            description = "Returns a Paged response of all the products")
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<PagedResult<ProductResponse>>> getAllProducts(

            @Parameter(description = "Page number (starts from 0)", example = "0")
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(description = "Number of items per page (max 100)", example = "10")
            @RequestParam(defaultValue = "10")
            int pageSize,

            @Parameter(
                    description = "Field to sort by",
                    example = "name",
                    schema = @Schema(
                            allowableValues = {"name", "price", "stockQuantity", "category"})
            )
            @RequestParam(defaultValue = "name") String sortBy,

            @Parameter(
                    description = "Sort direction",
                    example = "asc",
                    schema = @Schema(allowableValues = {"asc", "desc"})
            )
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {

        // Call service with individual parameters
        var response = productService.findAllProducts(page, pageSize, sortBy, sortDirection);

        return ResponseEntity.ok(response);
    }
}
