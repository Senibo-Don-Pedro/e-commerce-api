package com.senibo.e_commerce_api.service;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.PagedResult;
import com.senibo.e_commerce_api.dto.product.CreateProductRequest;
import com.senibo.e_commerce_api.dto.product.ProductResponse;
import com.senibo.e_commerce_api.dto.product.UpdateProductRequest;

import java.util.List;
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

    /**
     * Deletes a product by its ID.
     */
    void deleteProduct(UUID id);


    // === Public Read Methods ===

    /**
     * Finds a single product by its unique ID.
     */
    ApiSuccessResponse<ProductResponse> findProductById(UUID id);

    /**
     * Find all products with pagination and sorting
     *
     * @param page          Page number (0-based)
     * @param pageSize      Number of items per page
     * @param sortBy        Field to sort by (e.g., "name", "price", "createdAt")
     * @param sortDirection Sort direction ("asc" or "desc")
     * @return Paginated list of products
     */
    ApiSuccessResponse<PagedResult<ProductResponse>> findAllProducts(
            int page,
            int pageSize,
            String sortBy,
            String sortDirection
    );

    /**
     * Finds a list of products belonging to a specific category.
     */
    ApiSuccessResponse<List<ProductResponse>> findProductsByCategory(String category);

    /**
     * Searches for products by name (returns a simple list).
     */
    ApiSuccessResponse<List<ProductResponse>> searchProductsByName(String name);

}
