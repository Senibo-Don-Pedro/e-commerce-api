package com.senibo.e_commerce_api.controller.external;

import com.senibo.e_commerce_api.dto.ApiResponseDto;
import com.senibo.e_commerce_api.dto.product.ProductResponse;
import com.senibo.e_commerce_api.dto.product.ProductSearchRequest;
import com.senibo.e_commerce_api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@Validated
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<ProductResponse>>> searchProducts(
            @Valid ProductSearchRequest searchRequest,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        
        log.info("Searching products with filters: {}", searchRequest);
        Page<ProductResponse> products = productService.searchProducts(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponseDto.success("Products retrieved successfully", products));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ProductResponse>> getProduct(@PathVariable UUID id) {
        log.info("Fetching product with id: {}", id);
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponseDto.success("Product retrieved successfully", product));
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponseDto<Page<ProductResponse>>> getProductsByCategory(
            @PathVariable UUID categoryId,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        
        log.info("Fetching products for category: {}", categoryId);
        Page<ProductResponse> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponseDto.success("Category products retrieved successfully", products));
    }
    
    @GetMapping("/featured")
    public ResponseEntity<ApiResponseDto<List<ProductResponse>>> getFeaturedProducts() {
        log.info("Fetching featured products");
        List<ProductResponse> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(ApiResponseDto.success("Featured products retrieved successfully", products));
    }
}
