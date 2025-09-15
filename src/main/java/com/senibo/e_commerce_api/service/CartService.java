package com.senibo.e_commerce_api.service;

import com.senibo.e_commerce_api.dto.cart.AddToCartRequest;
import com.senibo.e_commerce_api.dto.cart.CartOperationResponse;
import com.senibo.e_commerce_api.dto.cart.CartResponse;
import com.senibo.e_commerce_api.dto.cart.UpdateCartItemRequest;
import com.senibo.e_commerce_api.model.cart.Cart;

import java.util.UUID;

public interface CartService {
    
    // Core cart operations
    CartResponse getCart(UUID userId);
    CartOperationResponse addToCart(UUID userId, AddToCartRequest request);
    CartOperationResponse updateCartItem(UUID userId, UUID productId, UpdateCartItemRequest request);
    CartOperationResponse removeFromCart(UUID userId, UUID productId);
    CartOperationResponse clearCart(UUID userId);
    
    // Helper methods for other services (like OrderService later)
    Cart getCartEntity(UUID userId);
    void createCartForUser(UUID userId);
    boolean isCartEmpty(UUID userId);
    
    // Internal operations (used by other parts of the system)
    void releaseAllReservedStock(UUID userId); // When user logs out or cart expires
}
