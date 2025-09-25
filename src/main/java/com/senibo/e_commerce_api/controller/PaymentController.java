package com.senibo.e_commerce_api.controller;

import com.senibo.e_commerce_api.client.dto.PaymentResponse;
import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.security.services.UserDetailsImpl;
import com.senibo.e_commerce_api.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "payment-controller", description = "Endpoints for handling payments.")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Initialize a payment transaction",
            description = "Creates an order from the user's cart and returns a Paystack URL for payment.")
    @PostMapping("/initialize")
    public ResponseEntity<ApiSuccessResponse<PaymentResponse>> initializePayment(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String paymentUrl = paymentService.initializePayment(userDetails.getId());

        PaymentResponse paymentResponse = new PaymentResponse(paymentUrl);

        ApiSuccessResponse<PaymentResponse> response = new ApiSuccessResponse<>(
                true,
                "Payment initialized successfully. Redirect user to the provided URL.",
                paymentResponse
        );

        return ResponseEntity.ok(response);
    }
}
