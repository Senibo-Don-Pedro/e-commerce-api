package com.senibo.e_commerce_api.service;

import com.senibo.e_commerce_api.dto.cart.AddItemToCartRequest;
import com.senibo.e_commerce_api.dto.cart.CartResponse;

import java.util.UUID;

public interface CartService {
    CartResponse addItemToCart(UUID userId, AddItemToCartRequest request);

    CartResponse getCartForUser(UUID userId);

    CartResponse removeItemFromCart(UUID userId, UUID itemId);
}
