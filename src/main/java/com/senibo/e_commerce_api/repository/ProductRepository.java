package com.senibo.e_commerce_api.repository;

import com.senibo.e_commerce_api.model.product.Product;
import com.senibo.e_commerce_api.model.product.ProductCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>,
        JpaSpecificationExecutor<Product> {

    /**
     * Finds a single product by its unique SKU.
     * Use this when you need exactly one result.
     */
    Optional<Product> findBySku(String sku);

    /**
     * Add this new method for an efficient uniqueness check
     */
    boolean existsBySku(String sku);

    /**
     * Checks to see if the product exists by name
     */
    boolean existsByName(String name); // <-- Add this

    /**
     * Searches for products whose name contains the given string, ignoring case.
     * Use this for user-facing search bars. It correctly returns a list.
     */
    List<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Finds all products in a given category.
     * This is another example of a safe method that returns a list.
     */
    List<Product> findByCategory(ProductCategory category, Pageable pageable);

}
