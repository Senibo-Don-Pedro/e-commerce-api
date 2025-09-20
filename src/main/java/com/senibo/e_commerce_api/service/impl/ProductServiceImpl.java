package com.senibo.e_commerce_api.service.impl;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.PagedResult;
import com.senibo.e_commerce_api.dto.product.CreateProductRequest;
import com.senibo.e_commerce_api.dto.product.ProductResponse;
import com.senibo.e_commerce_api.dto.product.UpdateProductRequest;
import com.senibo.e_commerce_api.exception.general.NotFoundException;
import com.senibo.e_commerce_api.exception.general.ResourceConflictException;
import com.senibo.e_commerce_api.model.product.Product;
import com.senibo.e_commerce_api.repository.ProductRepository;
import com.senibo.e_commerce_api.service.ProductService;
import com.senibo.e_commerce_api.util.PaginationValidator;
import com.senibo.e_commerce_api.util.SkuGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SkuGenerator skuGenerator;
    private final PaginationValidator paginationValidator;


    @Override
    public ApiSuccessResponse<ProductResponse> createProduct(CreateProductRequest request) {

        if (productRepository.existsByName(request.name())) {
            throw new ResourceConflictException(
                    String.format("Product with name %s already exists", request.name())
            );
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

    @Override
    public ApiSuccessResponse<ProductResponse> updateProduct(UUID id,
                                                             UpdateProductRequest request) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Product with id %s not found", id))
        );

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

    @Override
    public void deleteProduct(UUID id) {

    }

    @Override
    public ApiSuccessResponse<ProductResponse> findProductById(UUID id) {
        return null;
    }

    @Override
    public ApiSuccessResponse<PagedResult<ProductResponse>> findAllProducts(
            int page,
            int pageSize,
            String sortBy,
            String sortDirection
    ) {

        // Step 1: Use validator to create Pageable (handles the messy stuff)
        Pageable pageable = paginationValidator.createPageable(page,
                                                               pageSize,
                                                               sortBy,
                                                               sortDirection);

        // Step 2: Get data from database
        Page<Product> productPage = productRepository.findAll(pageable);

        // Step 3: Convert to response DTOs
        Page<ProductResponse> productResponsePage = productPage.map(ProductResponse::fromEntity);

        // Step 4: Convert to our clean format
        PagedResult<ProductResponse> pagedResult = PagedResult.from(productResponsePage);

        // Step 5: Return success response
        return new ApiSuccessResponse<>(
                true,
                "Products retrieved successfully",
                pagedResult
        );
    }

    @Override
    public ApiSuccessResponse<List<ProductResponse>> findProductsByCategory(String category) {
        return null;
    }

    @Override
    public ApiSuccessResponse<List<ProductResponse>> searchProductsByName(String name) {
        return null;
    }


}
