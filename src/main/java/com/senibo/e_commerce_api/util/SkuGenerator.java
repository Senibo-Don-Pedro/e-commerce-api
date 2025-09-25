package com.senibo.e_commerce_api.util;

import com.senibo.e_commerce_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A utility component for generating unique Stock Keeping Units (SKUs) for products.
 * <p>
 * It creates a formatted SKU and ensures its uniqueness by checking against the database.
 */
@Component
@RequiredArgsConstructor
public class SkuGenerator {

    private final ProductRepository productRepository;

    /**
     * Generates a unique SKU for a product based on its name and category.
     * The format is [CAT]-[NAM]-[RANDOM_NUM]. It repeatedly generates a new SKU
     * until it finds one that does not already exist in the database.
     *
     * @param productName  The name of the product.
     * @param categoryName The name of the product's category.
     * @return A unique, formatted SKU string.
     */
    public String generateUniqueSku(String productName, String categoryName) {
        String sku;
        do {
            String namePrefix = createPrefix(productName);
            String categoryPrefix = createPrefix(categoryName);
            int randomNumber = ThreadLocalRandom.current().nextInt(10000, 100000);
            sku = categoryPrefix + "-" + namePrefix + "-" + randomNumber;
        } while (productRepository.existsBySku(sku));
        return sku;
    }

    /**
     * Creates a 3-character, uppercase, alphanumeric prefix from an input string.
     * It sanitizes the input by removing non-alphanumeric characters and safely
     * handles strings that are shorter than 3 characters.
     *
     * @param input The string to generate a prefix from.
     * @return A sanitized, 3-character (or less) uppercase prefix.
     */
    private String createPrefix(String input) {
        String sanitized = input.replaceAll("[^a-zA-Z0-9]", "");
        int length = Math.min(sanitized.length(), 3);
        return sanitized.substring(0, length).toUpperCase();
    }
}
