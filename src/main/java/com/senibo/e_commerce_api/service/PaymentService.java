package com.senibo.e_commerce_api.service;

import java.util.UUID;

public interface PaymentService {

    /**
     * Creates an order from the user's cart and initializes a payment transaction with Paystack.
     *
     * @param userId The ID of the user who is checking out.
     * @return The authorization URL from Paystack for the user to complete the payment.
     */
    String initializePayment(UUID userId);

    /**
     * Handles and verifies incoming webhook events from Paystack.
     *
     * @param signature   The signature from the 'x-paystack-signature' header.
     * @param requestBody The raw JSON payload from the request.
     */
    void handlePaystackWebhook(String signature, String requestBody);
}
