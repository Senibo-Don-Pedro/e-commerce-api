package com.senibo.e_commerce_api.repository;

import com.senibo.e_commerce_api.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    // Find by slug
    Optional<Category> findBySlug(String slug);
    Optional<Category> findBySlugAndIsActiveTrue(String slug);

    // Check existence
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, UUID id);

    // Find active categories
    List<Category> findByIsActiveTrueOrderBySortOrderAsc();

    // Find all ordered
    List<Category> findAllByOrderBySortOrderAsc();

    // Custom query for counting products
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category AND p.isActive = true")
    Integer countActiveProductsByCategory(@Param("category") Category category);
}
