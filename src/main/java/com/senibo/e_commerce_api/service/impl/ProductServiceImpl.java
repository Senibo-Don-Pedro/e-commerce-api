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
    public ApiSuccessResponse<PagedResult<ProductResponse>> findAllProducts(
            int page,
            int pageSize,
            String sortBy,
            String sortDirection,
            Optional<String> searchTerm,
            Optional<ProductCategory> category,
            Optional<BigDecimal> minPrice,
            Optional<BigDecimal> maxPrice
    ) {
        // Step 1: Create the Pageable object using your validator
        Pageable pageable = paginationValidator.createPageable(page,
                                                               pageSize,
                                                               sortBy,
                                                               sortDirection);

        // Step 2: Build the dynamic filter query using your specification builder
        Specification<Product> spec = ProductSpecification.build(searchTerm,
                                                                 category,
                                                                 minPrice,
                                                                 maxPrice);

        // Step 3: Fetch the data from the repository using both the filter and pagination
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        // Step 4: Map the result to your clean PagedResult DTO
        Page<ProductResponse> productResponsePage = productPage.map(ProductResponse::fromEntity);
        PagedResult<ProductResponse> pagedResult = PagedResult.from(productResponsePage);

        // Step 5: Return the final success response
        return new ApiSuccessResponse<>(true, "Products retrieved successfully", pagedResult);
    }


}
