package com.senibo.e_commerce_api.controller;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.product.CreateProductRequest;
import com.senibo.e_commerce_api.dto.product.ProductResponse;
import com.senibo.e_commerce_api.dto.product.UpdateProductRequest;
import com.senibo.e_commerce_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "admin-controller", description = "Endpoints for managing products.")
public class AdminController {

    private final ProductService productService;

    @Operation(summary = "Create a new product",
            description = "Adds a new product to the catalog. Requires ADMIN role.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/products") // <-- Add the HTTP method and path
    public ResponseEntity<ApiSuccessResponse<ProductResponse>> createProduct(
            @Valid
            @RequestBody CreateProductRequest createProductRequest
    ) {
        ApiSuccessResponse<ProductResponse> response = productService.createProduct(
                createProductRequest);

        // Return the response from the service, wrapped in a ResponseEntity with a 201 CREATED status
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a product",
            description = "Updates an already existing product in the catalog. Requires ADMIN " +
                    "role.")
    @PutMapping("/products/{id}")
    public ResponseEntity<ApiSuccessResponse<ProductResponse>> updateProduct(
            @PathVariable UUID id,

            @Valid
            @RequestBody UpdateProductRequest updateProductRequest
    ) {
        ApiSuccessResponse<ProductResponse> response = productService.updateProduct(id,
                                                                                    updateProductRequest);

        return ResponseEntity.ok(response);
    }
}
