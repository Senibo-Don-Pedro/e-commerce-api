package com.senibo.e_commerce_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the E-commerce API application.
 * <p>
 * This class uses the {@link SpringBootApplication} annotation, which enables
 * auto-configuration, component scanning, and other Spring Boot features.
 */
@SpringBootApplication
public class ECommerceApiApplication {

    /**
     * The main method which serves as the entry point for the Java application.
     * It delegates to Spring Boot's {@link SpringApplication} class to bootstrap the application.
     *
     * @param args command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ECommerceApiApplication.class, args);
    }

}
