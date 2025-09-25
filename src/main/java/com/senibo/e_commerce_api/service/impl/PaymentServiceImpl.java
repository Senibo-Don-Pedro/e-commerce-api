package com.senibo.e_commerce_api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senibo.e_commerce_api.client.PaystackClient;
import com.senibo.e_commerce_api.client.dto.PaystackInitRequest;
import com.senibo.e_commerce_api.client.dto.PaystackInitResponse;
import com.senibo.e_commerce_api.client.dto.PaystackWebhookPayload;
import com.senibo.e_commerce_api.exception.general.NotFoundException;
import com.senibo.e_commerce_api.model.order.Order;
import com.senibo.e_commerce_api.model.order.OrderStatus;
import com.senibo.e_commerce_api.repository.OrderRepository;
import com.senibo.e_commerce_api.service.OrderService;
import com.senibo.e_commerce_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Implements the service for handling payment processing via Paystack.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final PaystackClient paystackClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.app.paystack-test-secret-key}")
    private String paystackSecretKey;

    /**
     * Initializes a payment transaction for a user's order.
     *
     * @param userId The ID of the user initiating the payment.
     * @return The payment authorization URL to which the user should be redirected.
     */
    @Override
    @Transactional
    public String initializePayment(UUID userId) {
        Order order = orderService.createOrderFromCart(userId);

        int amountInKobo = order.getTotalAmount().multiply(new BigDecimal("100")).intValue();

        PaystackInitRequest paystackRequest = new PaystackInitRequest(
                order.getUser().getEmail(),
                amountInKobo,
                order.getId().toString() // Use internal order ID as the unique reference
        );

        PaystackInitResponse paystackResponse = paystackClient.initializeTransaction(paystackRequest);

        // Link our order to Paystack's transaction reference
        order.setPaymentReference(paystackResponse.data().reference());
        orderRepository.save(order);

        return paystackResponse.data().authorization_url();
    }

    /**
     * Handles incoming webhook events from Paystack.
     *
     * @param signature   The X-Paystack-Signature header for verification.
     * @param requestBody The raw JSON body of the webhook request.
     * @throws SecurityException if the signature is invalid.
     * @throws RuntimeException  if webhook processing fails for other reasons.
     */
    @Override
    @Transactional
    public void handlePaystackWebhook(String signature, String requestBody) {
        if (!isSignatureValid(signature, requestBody)) {
            log.error("Invalid Paystack signature received. Potential security breach.");
            throw new SecurityException("Invalid Paystack signature");
        }

        try {
            PaystackWebhookPayload payload = objectMapper.readValue(requestBody,
                                                                    PaystackWebhookPayload.class);

            if ("charge.success".equals(payload.event())) {
                log.info("Received successful charge event for reference: {}",
                         payload.data().reference());

                Order order = orderRepository.findByPaymentReference(payload.data().reference())
                                             .orElseThrow(() -> new NotFoundException(
                                                     "Order not found for payment reference: " + payload.data()
                                                                                                        .reference()));

                order.setOrderStatus(OrderStatus.PAID);
                orderRepository.save(order);
                log.info("Order {} has been updated to PAID.", order.getId());
            }
        } catch (Exception e) {
            log.error("Error processing Paystack webhook", e);
            throw new RuntimeException("Webhook processing failed", e);
        }
    }

    /**
     * Verifies the webhook signature using an HMAC-SHA512 algorithm.
     *
     * @param signature   The signature from the X-Paystack-Signature header.
     * @param requestBody The raw request body.
     * @return {@code true} if the signature is valid, {@code false} otherwise.
     */
    private boolean isSignatureValid(String signature, String requestBody) {
        try {
            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(paystackSecretKey.getBytes(StandardCharsets.UTF_8),
                                                        "HmacSHA512");
            sha512Hmac.init(secretKey);
            byte[] hash = sha512Hmac.doFinal(requestBody.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString().equals(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error verifying Paystack signature", e);
            return false;
        }
    }
}
