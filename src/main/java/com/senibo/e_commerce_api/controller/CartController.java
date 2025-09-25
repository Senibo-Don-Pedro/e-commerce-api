package com.senibo.e_commerce_api.controller;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.cart.AddItemToCartRequest;
import com.senibo.e_commerce_api.dto.cart.CartResponse;
import com.senibo.e_commerce_api.security.services.UserDetailsImpl;
import com.senibo.e_commerce_api.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "cart-controller", description = "Endpoints for managing the user's shopping cart.")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Add an item to the current user's cart",
            description = "Adds a product to the cart or updates its quantity if it already exists.")
    @PostMapping("/items")
    public ResponseEntity<ApiSuccessResponse<CartResponse>> addItemToCart(
            @Valid @RequestBody AddItemToCartRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 1. Call the service with the current user's ID and the request data
        CartResponse updatedCart = cartService.addItemToCart(userDetails.getId(), request);

        // 2. Wrap the result in your standard success response
        ApiSuccessResponse<CartResponse> response = new ApiSuccessResponse<>(
                true,
                "Item added to cart successfully",
                updatedCart
        );

        // 3. Return the final ResponseEntity
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get the current user's cart")
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<CartResponse>> getUserCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CartResponse cart = cartService.getCartForUser(userDetails.getId());

        ApiSuccessResponse<CartResponse> response = new ApiSuccessResponse<>(
                true,
                "Cart retrieved successfully",
                cart
        );

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Remove an item from the cart")
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiSuccessResponse<CartResponse>> removeItemFromCart(
            @PathVariable UUID itemId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CartResponse updatedCart = cartService.removeItemFromCart(userDetails.getId(), itemId);

        ApiSuccessResponse<CartResponse> response = new ApiSuccessResponse<>(
                true,
                "Item removed from cart successfully",
                updatedCart
        );

        return ResponseEntity.ok(response);
    }
}
