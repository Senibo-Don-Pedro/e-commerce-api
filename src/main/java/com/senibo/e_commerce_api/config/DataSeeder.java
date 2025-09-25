package com.senibo.e_commerce_api.config;

import com.github.javafaker.Faker;
import com.senibo.e_commerce_api.model.auth.Role;
import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.model.product.Product;
import com.senibo.e_commerce_api.model.product.ProductCategory;
import com.senibo.e_commerce_api.repository.ProductRepository;
import com.senibo.e_commerce_api.repository.UserRepository;
import com.senibo.e_commerce_api.util.SkuGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Seeds the database with initial data on application startup.
 * <p>
 * This component implements {@link CommandLineRunner}, which ensures its {@code run} method
 * is executed once the Spring application context is loaded. It is annotated with
 * {@code @Profile("dev")} to ensure it only runs during development and not in production.
 * It seeds default users (admin, customer) and a set of mock products.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SkuGenerator skuGenerator;

    /**
     * The entry point for the seeding process, executed on application startup.
     *
     * @param args incoming command line arguments
     */
    @Override
    public void run(String... args) {
        log.info("Starting data seeding process...");
        seedUsers();
        seedProducts();
        log.info("Finished data seeding process.");
    }

    /**
     * Seeds the database with a default admin and a default customer if they do not already exist.
     */
    private void seedUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                             .firstname("Admin").lastname("User").username("admin")
                             .email("admin@example.com")
                             .password(passwordEncoder.encode("password123"))
                             .role(Role.ADMIN)
                             .build();
            userRepository.save(admin);
            log.info("Admin user created.");
        }

        if (!userRepository.existsByUsername("johndoe")) {
            User customer = User.builder()
                                .firstname("John").lastname("Doe").username("johndoe")
                                .email("john.doe@example.com")
                                .password(passwordEncoder.encode("password123"))
                                .role(Role.CUSTOMER)
                                .build();
            userRepository.save(customer);
            log.info("Customer user created.");
        }
    }

    /**
     * Seeds the database with 200 randomly generated products if no products exist.
     * Uses the JavaFaker library to generate realistic mock product data.
     */
    private void seedProducts() {
        if (productRepository.count() == 0) {
            log.info("Seeding 200 random products...");
            Faker faker = new Faker();
            List<ProductCategory> categories = Arrays.asList(ProductCategory.values());

            for (int i = 0; i < 200; i++) {
                String productName = faker.commerce().productName();
                ProductCategory category = categories.get(faker.random()
                                                               .nextInt(categories.size()));

                Product product = Product.builder()
                                         .name(productName)
                                         .description(faker.lorem().sentence(15))
                                         .price(new BigDecimal(faker.commerce()
                                                                    .price(10.00, 2000.00)))
                                         .stockQuantity(faker.number().numberBetween(0, 500))
                                         .category(category)
                                         .sku(skuGenerator.generateUniqueSku(productName,
                                                                             category.name()))
                                         .build();

                productRepository.save(product);
            }
        }
    }
}
