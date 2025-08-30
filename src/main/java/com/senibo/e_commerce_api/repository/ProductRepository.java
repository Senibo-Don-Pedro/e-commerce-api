package com.senibo.e_commerce_api.repository;

import com.senibo.e_commerce_api.model.category.Category;
import com.senibo.e_commerce_api.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    // Find by SKU
    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, UUID id);

    // Find by category
    Page<Product> findByCategoryAndIsActiveTrue(Category category, Pageable pageable);

    Page<Product> findByCategoryIdAndIsActiveTrue(UUID categoryId, Pageable pageable);

    // Check if category has products
    boolean existsByCategoryAndIsActiveTrue(Category category);

    // Advanced search functionality
    @Query("SELECT p FROM Product p WHERE " + "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " + "(:categoryId IS NULL OR p.category.id = :categoryId) AND " + "(:minPrice IS NULL OR p.price >= :minPrice) AND " + "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " + "(:inStock IS NULL OR (:inStock = true AND (p.stockQuantity - p.reservedQuantity) > 0)) AND " + "p.isActive = true")
    Page<Product> searchProducts(@Param("name") String name,
                                 @Param("categoryId") UUID categoryId,
                                 @Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice,
                                 @Param("inStock") Boolean inStock,
                                 Pageable pageable);

    // Featured products - you can customize this logic
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND (p.stockQuantity - p.reservedQuantity) > 5 ORDER BY p.createdAt DESC")
    List<Product> findFeaturedProducts(Pageable pageable);

    // Stock management queries
    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = :stockQuantity WHERE p.id = :productId")
    void updateStock(@Param("productId") UUID productId,
                     @Param("stockQuantity") Integer stockQuantity);

    @Modifying
    @Query("UPDATE Product p SET p.reservedQuantity = p.reservedQuantity + :quantity WHERE p.id = :productId AND (p.stockQuantity - p.reservedQuantity) >= :quantity")
    int reserveStock(@Param("productId") UUID productId, @Param("quantity") Integer quantity);


    @Modifying
    @Query("UPDATE Product p SET p.reservedQuantity = p.reservedQuantity - :quantity WHERE p.id = :productId AND p.reservedQuantity >= :quantity")
    int releaseStock(@Param("productId") UUID productId, @Param("quantity") Integer quantity);
}
