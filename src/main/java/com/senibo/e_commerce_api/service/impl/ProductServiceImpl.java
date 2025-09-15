package com.senibo.e_commerce_api.service.impl;

import com.senibo.e_commerce_api.dto.category.CategoryResponse;
import com.senibo.e_commerce_api.dto.product.ProductRequest;
import com.senibo.e_commerce_api.dto.product.ProductResponse;
import com.senibo.e_commerce_api.dto.product.ProductSearchRequest;
import com.senibo.e_commerce_api.exception.product.InsufficientStockException;
import com.senibo.e_commerce_api.exception.product.InvalidProductOperationException;
import com.senibo.e_commerce_api.exception.product.ProductNotFoundException;
import com.senibo.e_commerce_api.model.category.Category;
import com.senibo.e_commerce_api.model.product.Product;
import com.senibo.e_commerce_api.repository.CategoryRepository;
import com.senibo.e_commerce_api.repository.ProductRepository;
import com.senibo.e_commerce_api.service.CategoryService;
import com.senibo.e_commerce_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product with name: {}", request.name());
        
        // Validate category exists and is active
        Category category = categoryService.findCategoryEntityById(request.categoryId());
        if (!category.getIsActive()) {
            throw new InvalidProductOperationException("Cannot create product in inactive category");
        }
        
        // Generate SKU if not provided
        String sku = request.sku() != null ? request.sku() : generateSKU(request.name());
        sku = ensureUniqueSKU(sku, null);
        
        Product product = Product.builder()
                                 .name(request.name())
                                 .sku(sku)
                                 .description(request.description())
                                 .price(request.price())
                                 .category(category)
                                 .stockQuantity(request.stockQuantity() != null ? request.stockQuantity() : 0)
                                 .reservedQuantity(0)
                                 .imageUrl(request.imageUrl())
                                 .weight(request.weight() != null ? request.weight() : 0.0)
                                 .isActive(true) // Always true for new products
                                 .build();
                
        Product saved = productRepository.save(product);
        log.info("Product created successfully with id: {} and SKU: {}", saved.getId(), saved.getSku());
        
        return mapToResponse(saved);
    }
    
    @Override
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        log.info("Updating product with id: {}", id);
        
        Product product = findProductEntityById(id);
        
        // Validate category exists and is active
        Category category = categoryService.findCategoryEntityById(request.categoryId());
        if (!category.getIsActive()) {
            throw new InvalidProductOperationException("Cannot assign product to inactive category");
        }
        
        // Handle SKU update if provided and different
        if (request.sku() != null && !request.sku().equals(product.getSku())) {
            String newSku = ensureUniqueSKU(request.sku(), id);
            product.setSku(newSku);
        }
        
        // Update fields
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(category);
        product.setImageUrl(request.imageUrl());
        product.setWeight(request.weight() != null ? request.weight() : product.getWeight());
        
        // Update stock if provided (careful not to override reserved quantity logic)
        if (request.stockQuantity() != null) {
            // Ensure we don't set stock below reserved quantity
            if (request.stockQuantity() < product.getReservedQuantity()) {
                throw new InsufficientStockException(
                    "Cannot set stock quantity below reserved quantity (" + product.getReservedQuantity() + ")");
            }
            product.setStockQuantity(request.stockQuantity());
        }
        
        // Only update isActive if provided (for updates only)
        if (request.isActive() != null) {
            product.setIsActive(request.isActive());
        }
        
        Product updated = productRepository.save(product);
        log.info("Product updated successfully: {}", id);
        
        return mapToResponse(updated);
    }
    
    @Override
    public void deleteProduct(UUID id) {
        log.info("Attempting to delete product with id: {}", id);
        
        Product product = findProductEntityById(id);
        
        // Business rule: Can't delete product with reserved stock (items in carts)
        if (product.getReservedQuantity() > 0) {
            throw new InvalidProductOperationException(
                "Cannot delete product '" + product.getName() + "' because it has reserved stock (items in carts)");
        }
        
        productRepository.delete(product);
        log.info("Product deleted successfully: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID id) {
        log.info("Fetching product with id: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
                
        // Only return active products to public
        if (!product.getIsActive()) {
            throw new ProductNotFoundException("Product not available");
        }
        
        return mapToResponse(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(ProductSearchRequest request,
                                                       Pageable pageable) {
        log.info("Searching products with criteria: {}", request);
        
        Page<Product> products = productRepository.searchProducts(
            request.name(),
            request.categoryId(),
            request.minPrice(),
            request.maxPrice(),
            request.inStock(),
            pageable
        );
        
        return products.map(this::mapToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(UUID categoryId, Pageable pageable) {
        log.info("Fetching products for category: {}", categoryId);
        
        // Validate category exists (this will throw exception if not found)
        categoryService.findCategoryEntityById(categoryId);
        
        Page<Product> products = productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);
        return products.map(this::mapToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getFeaturedProducts() {
        log.info("Fetching featured products");
        
        Pageable limit = PageRequest.of(0, 10); // Top 10 featured products
        List<Product> products = productRepository.findFeaturedProducts(limit);
        
        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public void updateStock(UUID productId, Integer newStock) {
        log.info("Updating stock for product {}: new stock = {}", productId, newStock);
        
        Product product = findProductEntityById(productId);
        
        // Ensure we don't set stock below reserved quantity
        if (newStock < product.getReservedQuantity()) {
            throw new InsufficientStockException(
                "Cannot set stock quantity (" + newStock + ") below reserved quantity (" + product.getReservedQuantity() + ")");
        }
        
        product.setStockQuantity(newStock);
        productRepository.save(product);
        
        log.info("Stock updated successfully for product {}: {} -> {}", productId, product.getStockQuantity(), newStock);
    }
    
    @Override
    public void reserveStock(UUID productId, Integer quantity) {
        log.info("Reserving {} units of stock for product {}", quantity, productId);
        
        int rowsUpdated = productRepository.reserveStock(productId, quantity);
        
        if (rowsUpdated == 0) {
            Product product = findProductEntityById(productId);
            throw new InsufficientStockException(
                "Cannot reserve " + quantity + " units. Available stock: " + product.getAvailableStock());
        }
        
        log.info("Successfully reserved {} units for product {}", quantity, productId);
    }
    
    @Override
    public void releaseStock(UUID productId, Integer quantity) {
        log.info("Releasing {} units of reserved stock for product {}", quantity, productId);
        
        int rowsUpdated = productRepository.releaseStock(productId, quantity);
        
        if (rowsUpdated == 0) {
            Product product = findProductEntityById(productId);
            throw new InvalidProductOperationException(
                "Cannot release " + quantity + " units. Reserved stock: " + product.getReservedQuantity());
        }
        
        log.info("Successfully released {} units for product {}", quantity, productId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        log.info("Fetching all products (including inactive)");
        
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::mapToResponse);
    }
    
    @Override
    public void toggleProductStatus(UUID id) {
        log.info("Toggling status for product: {}", id);
        
        Product product = findProductEntityById(id);
        
        // If deactivating, release any reserved stock (business decision)
        if (product.getIsActive() && product.getReservedQuantity() > 0) {
            log.warn("Deactivating product {} with {} reserved units - releasing reserved stock", 
                    id, product.getReservedQuantity());
            product.setReservedQuantity(0);
        }
        
        product.setIsActive(!product.getIsActive());
        productRepository.save(product);
        
        log.info("Product {} status changed to: {}", id, product.getIsActive());
    }
    
    // Helper methods
    private Product findProductEntityById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }
    
    private String generateSKU(String productName) {
        // Generate SKU from product name + timestamp
        String baseSku = productName.toLowerCase()
                .replaceAll("[^a-z0-9]", "")
                .substring(0, Math.min(productName.length(), 6))
                .toUpperCase();
                
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8); // Last 5 digits
        return baseSku + "-" + timestamp;
    }
    
    private String ensureUniqueSKU(String baseSku, UUID excludeId) {
        String sku = baseSku;
        int counter = 1;
        
        while (skuExists(sku, excludeId)) {
            sku = baseSku + "-" + counter;
            counter++;
        }
        
        return sku;
    }
    
    private boolean skuExists(String sku, UUID excludeId) {
        if (excludeId == null) {
            return productRepository.existsBySku(sku);
        }
        return productRepository.existsBySkuAndIdNot(sku, excludeId);
    }
    
    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getSku(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity(),
            product.getReservedQuantity(),
            product.getAvailableStock(),
            product.getImageUrl(),
            product.getIsActive(),
            product.getWeight(),
            mapCategoryToSummary(product.getCategory()),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }

    private CategoryResponse mapCategoryToSummary(Category category) {
        Integer productCount = categoryRepository.countActiveProductsByCategory(category);
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getSlug(),
            category.getDescription(),
            category.getIsActive(),
            category.getSortOrder(),
            productCount,
            category.getCreatedAt(),
            category.getUpdatedAt()
        );
    }
}
