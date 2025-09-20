package com.senibo.e_commerce_api.util;

import com.senibo.e_commerce_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class SkuGenerator {

    private final ProductRepository productRepository;

    public String generateUniqueSku(String productName, String categoryName) {
        String sku;
        do {
            // Use the new helper method for cleaner code
            String namePrefix = createPrefix(productName);
            String categoryPrefix = createPrefix(categoryName);
            int randomNumber = ThreadLocalRandom.current().nextInt(10000, 100000);
            sku = categoryPrefix + "-" + namePrefix + "-" + randomNumber;
        } while (productRepository.existsBySku(sku));
        return sku;
    }

    /**
     * Creates a 3-character, uppercase, alphanumeric prefix from an input string.
     * Safely handles strings that are shorter than 3 characters.
     */
    private String createPrefix(String input) {
        // Sanitize the string to remove non-alphanumeric characters
        String sanitized = input.replaceAll("[^a-zA-Z0-9]", "");

        // Safely get the first 3 characters, or fewer if the string is too short
        int length = Math.min(sanitized.length(), 3);

        return sanitized.substring(0, length).toUpperCase();
    }
}
