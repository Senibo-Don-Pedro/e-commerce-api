package com.senibo.e_commerce_api.controller;

import com.senibo.e_commerce_api.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Tag(name = "webhook-controller",
        description = "Endpoints for receiving webhook notifications from external services.")
public class WebhookController {

    private final PaymentService paymentService;

    @Operation(summary = "Handle Paystack webhook events",
            description = "This endpoint receives and processes webhook notifications from Paystack.")
    @PostMapping("/paystack")
    public ResponseEntity<Void> handlePaystackWebhook(
            @RequestBody String requestBody,
            @RequestHeader("x-paystack-signature") String signature
    ) {
        // Pass the raw body and signature to the service for verification and processing
        paymentService.handlePaystackWebhook(signature, requestBody);

        // Always return a 200 OK to acknowledge receipt of the webhook
        return ResponseEntity.ok().build();
    }
}
