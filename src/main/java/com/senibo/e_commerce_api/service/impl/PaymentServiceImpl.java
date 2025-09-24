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

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final PaystackClient paystackClient;
    private final ObjectMapper objectMapper; // For parsing the JSON string

    @Value("${spring.app.paystack-test-secret-key}")
    private String paystackSecretKey;

    @Override
    @Transactional
    public String initializePayment(UUID userId) {
        // 1. Create a permanent order from the user's cart.
        Order order = orderService.createOrderFromCart(userId);

        // 2. Prepare the request for Paystack.
        // Convert the total amount from Naira (BigDecimal) to Kobo (Integer).
        int amountInKobo = order.getTotalAmount().multiply(new BigDecimal("100")).intValue();

        PaystackInitRequest paystackRequest = new PaystackInitRequest(
                order.getUser().getEmail(),
                amountInKobo,
                order.getId().toString() // Use our internal order ID as the unique reference
        );

        // 3. Call Paystack to initialize the transaction.
        PaystackInitResponse paystackResponse = paystackClient.initializeTransaction(paystackRequest);

        // 4. (Crucial Step) Link our order to Paystack's transaction.
        // Save the reference from Paystack in our order record.
        order.setPaymentReference(paystackResponse.data().reference());
        orderRepository.save(order);

        // 5. Return the payment URL to the controller.
        return paystackResponse.data().authorization_url();
    }

    @Override
    @Transactional
    public void handlePaystackWebhook(String signature, String requestBody) {
        // 1. SECURITY: Verify the signature to ensure the request is from Paystack
        if (!isSignatureValid(signature, requestBody)) {
            log.error("Invalid Paystack signature received. Potential security breach.");
            throw new SecurityException("Invalid Paystack signature");
        }

        try {
            // 2. Parse the JSON string into our DTO
            PaystackWebhookPayload payload = objectMapper.readValue(requestBody,
                                                                    PaystackWebhookPayload.class);

            // 3. Check if the event is a successful charge
            if ("charge.success".equals(payload.event())) {
                log.info("Received successful charge event for reference: {}",
                         payload.data().reference());

                // 4. Find the corresponding order in our database
                Order order = orderRepository.findByPaymentReference(payload.data().reference())
                                             .orElseThrow(() -> new NotFoundException(
                                                     "Order not found for payment reference: " + payload.data()
                                                                                                        .reference()));

                // 5. Update the order status to PAID
                order.setOrderStatus(OrderStatus.PAID);
                orderRepository.save(order);
                log.info("Order {} has been updated to PAID.", order.getId());

                // TODO: In a real application, you would also trigger other actions here,
                // such as sending a confirmation email to the user.
            }

        } catch (Exception e) {
            log.error("Error processing Paystack webhook", e);
            // We throw a runtime exception to indicate a processing failure.
            // Paystack might retry sending the webhook if it gets an error response.
            throw new RuntimeException("Webhook processing failed", e);
        }
    }

    /**
     * Verifies the webhook signature using HMAC-SHA512 algorithm.
     */
    private boolean isSignatureValid(String signature, String requestBody) {
        try {
            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(paystackSecretKey.getBytes(StandardCharsets.UTF_8),
                                                        "HmacSHA512");
            sha512Hmac.init(secretKey);
            byte[] hash = sha512Hmac.doFinal(requestBody.getBytes(StandardCharsets.UTF_8));

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            // Compare the computed hash with the signature from the header
            return hexString.toString().equals(signature);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error verifying Paystack signature", e);
            return false;
        }
    }
}
