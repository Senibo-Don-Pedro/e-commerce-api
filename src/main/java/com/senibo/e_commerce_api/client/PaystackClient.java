package com.senibo.e_commerce_api.client;

import com.senibo.e_commerce_api.client.dto.PaystackInitRequest;
import com.senibo.e_commerce_api.client.dto.PaystackInitResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PaystackClient {

    private final WebClient webClient;

    /**
     * The recommended constructor. It takes all required dependencies as arguments.
     *
     * @param webClientBuilder  Spring's builder for creating WebClient instances.
     * @param paystackSecretKey The secret key, injected from application properties.
     */
    public PaystackClient(
            WebClient.Builder webClientBuilder,
            @Value("${spring.app.paystack-test-secret-key}") String paystackSecretKey
            // <-- 1. Inject key here
    ) {
        // 2. Configure the WebClient with the base URL and default headers that
        // will be sent with EVERY request made by this client.
        this.webClient = webClientBuilder
                .baseUrl("https://api.paystack.co")
                .defaultHeader("Authorization", "Bearer " + paystackSecretKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    /**
     * Calls Paystack's API to initialize a new transaction.
     *
     * @param request The request body containing email, amount, and reference.
     * @return The response from Paystack.
     */
    public PaystackInitResponse initializeTransaction(PaystackInitRequest request) {
        return webClient
                .post()
                .uri("/transaction/initialize")
                .bodyValue(request) // The Authorization header is now added automatically
                .retrieve()
                .bodyToMono(PaystackInitResponse.class)
                .block();
    }
}
