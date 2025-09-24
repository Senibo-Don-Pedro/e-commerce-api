// Create this in a new package, e.g., repository/specifications
package com.senibo.e_commerce_api.util;

import com.senibo.e_commerce_api.model.product.Product;
import com.senibo.e_commerce_api.model.product.ProductCategory;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductSpecification {

    /**
     * The main builder method that dynamically constructs the final specification
     * based on the provided filters.
     */
    public static Specification<Product> build(
            Optional<String> searchTerm,
            Optional<ProductCategory> category,
            Optional<BigDecimal> minPrice,
            Optional<BigDecimal> maxPrice
            // Add future optional filters here, e.g., Optional<String> brand
    ) {
        List<Specification<Product>> specs = new ArrayList<>();

        // Add individual specifications to the list only if the filter is present
        searchTerm.ifPresent(term -> specs.add(nameOrSkuContains(term)));
        category.ifPresent(c -> specs.add(hasCategory(c)));
        minPrice.ifPresent(p -> specs.add(priceGreaterThanOrEqual(p)));
        maxPrice.ifPresent(p -> specs.add(priceLessThanOrEqual(p)));
        // Add checks for future filters here

        // Combine all specifications in the list with an "AND" operation
        return specs.stream().reduce(Specification::and).orElse(null);
    }

    // --- Private "Lego Brick" Methods ---

    private static Specification<Product> nameOrSkuContains(String searchTerm) {
        String searchTermLower = "%" + searchTerm.toLowerCase() + "%";

        return (root, query, cb) ->
                cb.or(
                        // WHERE lower(product.name) LIKE %searchTerm%
                        cb.like(cb.lower(root.get("name")), searchTermLower),

                        // OR lower(product.sku) LIKE %searchTerm%
                        cb.like(cb.lower(root.get("sku")), searchTermLower)

                );
    }

    private static Specification<Product> hasCategory(ProductCategory category) {
        // Creates a criteria: WHERE product.category = :category
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category);
    }

    private static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        // Creates a criteria: WHERE product.price >= :minPrice
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    private static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        // Creates a criteria: WHERE product.price <= :maxPrice
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
    
}
