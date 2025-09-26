package com.senibo.e_commerce_api.service.impl;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.PagedResult;
import com.senibo.e_commerce_api.dto.product.CreateProductRequest;
import com.senibo.e_commerce_api.dto.product.ProductResponse;
import com.senibo.e_commerce_api.dto.product.UpdateProductRequest;
import com.senibo.e_commerce_api.exception.general.NotFoundException;
import com.senibo.e_commerce_api.exception.general.ResourceConflictException;
import com.senibo.e_commerce_api.model.product.Product;
import com.senibo.e_commerce_api.model.product.ProductCategory;
import com.senibo.e_commerce_api.repository.ProductRepository;
import com.senibo.e_commerce_api.service.ProductService;
import com.senibo.e_commerce_api.util.PaginationValidator;
import com.senibo.e_commerce_api.util.ProductSpecification;
import com.senibo.e_commerce_api.util.SkuGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * Implements the service for managing the product catalog.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SkuGenerator skuGenerator;
    private final PaginationValidator paginationValidator;

    /**
     * Creates a new product in the catalog.
     *
     * @param request DTO containing the new product's details.
     * @return A success response containing the created product's DTO.
     * @throws ResourceConflictException if a product with the same name already exists.
     */
    @Override
    public ApiSuccessResponse<ProductResponse> createProduct(CreateProductRequest request) {
        if (productRepository.existsByName(request.name())) {
            throw new ResourceConflictException("Product with name " + request.name() + " already exists");
        }

        String uniqueSku = skuGenerator.generateUniqueSku(request.name(),
                                                          request.category().toString());
        Product product = Product.builder()
                                 .name(request.name())
                                 .description(request.description())
                                 .price(request.price())
                                 .stockQuantity(request.stockQuantity())
                                 .category(request.category())
                                 .imageUrl(request.imageUrl())
                                 .sku(uniqueSku)
                                 .build();

        Product savedProduct = productRepository.save(product);
        ProductResponse response = ProductResponse.fromEntity(savedProduct);
        return new ApiSuccessResponse<>(true, "Product created successfully", response);
    }

    /**
     * Updates an existing product's details.
     *
     * @param id      The ID of the product to update.
     * @param request DTO containing the updated product details.
     * @return A success response containing the updated product's DTO.
     * @throws NotFoundException if no product with the given ID is found.
     */
    @Override
    public ApiSuccessResponse<ProductResponse> updateProduct(UUID id,
                                                             UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                                           .orElseThrow(() -> new NotFoundException(
                                                   "Product with id " + id + " not found"));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(request.category());
        product.setImageUrl(request.imageUrl());

        Product updatedProduct = productRepository.save(product);
        ProductResponse response = ProductResponse.fromEntity(updatedProduct);
        return new ApiSuccessResponse<>(true, "Product updated successfully", response);
    }

    /**
     * Finds and returns a paginated and filtered list of all products.
     *
     * @param page          The page number to retrieve.
     * @param pageSize      The number of products per page.
     * @param sortBy        The field to sort by.
     * @param sortDirection The direction of the sort (ASC or DESC).
     * @param searchTerm    Optional search term to filter by product name or description.
     * @param category      Optional category to filter by.
     * @param minPrice      Optional minimum price to filter by.
     * @param maxPrice      Optional maximum price to filter by.
     * @return A success response containing the paginated list of product DTOs.
     */
    @Override
    public ApiSuccessResponse<PagedResult<ProductResponse>> findAllProducts(
            int page, int pageSize, String sortBy, String sortDirection,
            Optional<String> searchTerm, Optional<ProductCategory> category,
            Optional<BigDecimal> minPrice, Optional<BigDecimal> maxPrice
    ) {
        Pageable pageable = paginationValidator.createPageable(page,
                                                               pageSize,
                                                               sortBy,
                                                               sortDirection);
        Specification<Product> spec = ProductSpecification.build(searchTerm,
                                                                 category,
                                                                 minPrice,
                                                                 maxPrice);
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        Page<ProductResponse> productResponsePage = productPage.map(ProductResponse::fromEntity);
        PagedResult<ProductResponse> pagedResult = PagedResult.from(productResponsePage);

        return new ApiSuccessResponse<>(true, "Products retrieved successfully", pagedResult);
    }

    /**
     * Gets a single product by its ID.
     *
     * @param id The ID of the product to get.
     * @return A success response containing the single product's DTO.
     * @throws NotFoundException if no product with the given ID is found.
     */
    @Override
    public ApiSuccessResponse<ProductResponse> findProductById(UUID id) {
        Product product = productRepository.findById(id)
                                           .orElseThrow(() -> new NotFoundException(
                                                   "Product not found"));

        ProductResponse response = ProductResponse.fromEntity(product);

        return new ApiSuccessResponse<>(true, "Product found successfully", response);
    }


}
