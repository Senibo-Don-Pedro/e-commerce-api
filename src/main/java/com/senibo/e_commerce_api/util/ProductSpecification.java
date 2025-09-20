package com.senibo.e_commerce_api.util;

import com.senibo.e_commerce_api.model.product.Product;
import com.senibo.e_commerce_api.model.product.ProductCategory;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Optional;

public class ProductSpecification {

    /**
     * The main builder method that constructs the final specification.
     *
     * @param category Optional category to filter by.
     * @param minPrice Optional minimum price.
     * @param maxPrice Optional maximum price.
     * @return The combined Specification for querying products.
     */
    public static Specification<Product> build(
            Optional<ProductCategory> category,
            Optional<BigDecimal> minPrice,
            Optional<BigDecimal> maxPrice
            // Add future optional filters here, e.g., Optional<String> brand
    ) {
        Specification<Product> spec = Specification.where(null);

        if (category.isPresent()) {
            spec = spec.and(hasCategory(category.get()));
        }
        if (minPrice.isPresent()) {
            spec = spec.and(priceGreaterThanOrEqual(minPrice.get()));
        }
        if (maxPrice.isPresent()) {
            spec = spec.and(priceLessThanOrEqual(maxPrice.get()));
        }
        // Add checks for future filters here

        return spec;
    }

    // --- Private "Lego Brick" Methods ---

    private static Specification<Product> hasCategory(ProductCategory category) {
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }

    private static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    private static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
