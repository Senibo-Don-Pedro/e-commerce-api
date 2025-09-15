package com.senibo.e_commerce_api.controller.admin;

import com.senibo.e_commerce_api.dto.ApiResponseDto;
import com.senibo.e_commerce_api.dto.product.ProductRequest;
import com.senibo.e_commerce_api.dto.product.ProductResponse;
import com.senibo.e_commerce_api.dto.product.UpdateStockRequest;
import com.senibo.e_commerce_api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasAuthority('ADMIN')")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AdminProductController {
    
    private final ProductService productService;
    
    @PostMapping
    public ResponseEntity<ApiResponseDto<ProductResponse>> createProduct(@Valid @RequestBody
                                                                         ProductRequest request) {
        log.info("Creating product with name: {}", request.name());
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Product created successfully", product));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ProductResponse>> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        log.info("Updating product with id: {}", id);
        ProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponseDto.success("Product updated successfully", product));
    }
    
    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponseDto<Void>> updateStock(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStockRequest request) {
        log.info("Updating stock for product {}: new stock = {}", id, request.stockQuantity());
        productService.updateStock(id, request.stockQuantity());
        return ResponseEntity.ok(ApiResponseDto.success("Product stock updated successfully", null));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(@PathVariable UUID id) {
        log.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponseDto.success("Product deleted successfully", null));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<ProductResponse>>> getAllProducts(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        log.info("Fetching all products (including inactive)");
        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponseDto.success("All products retrieved successfully", products));
    }
    
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponseDto<Void>> toggleStatus(@PathVariable UUID id) {
        log.info("Toggling status for product with id: {}", id);
        productService.toggleProductStatus(id);
        return ResponseEntity.ok(ApiResponseDto.success("Product status updated successfully", null));
    }
}
