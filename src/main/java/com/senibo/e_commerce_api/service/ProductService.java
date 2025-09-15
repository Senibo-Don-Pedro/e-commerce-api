package com.senibo.e_commerce_api.service;

import com.senibo.e_commerce_api.dto.product.ProductRequest;
import com.senibo.e_commerce_api.dto.product.ProductResponse;
import com.senibo.e_commerce_api.dto.product.ProductSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    // CRUD operations
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(UUID id, ProductRequest request);
    void deleteProduct(UUID id);
    ProductResponse getProductById(UUID id);
    
    // Public browsing
    Page<ProductResponse> searchProducts(ProductSearchRequest request, Pageable pageable);
    Page<ProductResponse> getProductsByCategory(UUID categoryId, Pageable pageable);
    List<ProductResponse> getFeaturedProducts();
    
    // Inventory management
    void updateStock(UUID productId, Integer newStock);
    void reserveStock(UUID productId, Integer quantity);
    void releaseStock(UUID productId, Integer quantity);
    
    // Admin operations
    Page<ProductResponse> getAllProducts(Pageable pageable);
    void toggleProductStatus(UUID id);
}
