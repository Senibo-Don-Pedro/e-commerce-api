package com.senibo.e_commerce_api.service;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.PagedResult;
import com.senibo.e_commerce_api.dto.product.CreateProductRequest;
import com.senibo.e_commerce_api.dto.product.ProductResponse;
import com.senibo.e_commerce_api.dto.product.UpdateProductRequest;
import com.senibo.e_commerce_api.model.product.ProductCategory;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    // === Admin CRUD Methods ===

    /**
     * Creates a new product based on the request data.
     */
    ApiSuccessResponse<ProductResponse> createProduct(CreateProductRequest request);

    /**
     * Updates an existing product identified by its ID.
     */
    ApiSuccessResponse<ProductResponse> updateProduct(UUID id, UpdateProductRequest request);


    // === Public Read Methods ===


    /**
     * Finds products with optional filters, pagination, and sorting.
     */
    ApiSuccessResponse<PagedResult<ProductResponse>> findAllProducts(
            int page,
            int pageSize,
            String sortBy,
            String sortDirection,
            Optional<String> searchTerm,
            Optional<ProductCategory> category,
            Optional<BigDecimal> minPrice,
            Optional<BigDecimal> maxPrice
    );

    ApiSuccessResponse<ProductResponse> findProductById(UUID id);

}
