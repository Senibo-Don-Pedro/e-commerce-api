package com.senibo.e_commerce_api.service.impl;

import com.senibo.e_commerce_api.dto.cart.AddItemToCartRequest;
import com.senibo.e_commerce_api.dto.cart.CartResponse;
import com.senibo.e_commerce_api.exception.general.InsufficientStockException;
import com.senibo.e_commerce_api.exception.general.NotFoundException;
import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.model.cart.Cart;
import com.senibo.e_commerce_api.model.cartItem.CartItem;
import com.senibo.e_commerce_api.model.product.Product;
import com.senibo.e_commerce_api.repository.CartRepository;
import com.senibo.e_commerce_api.repository.ProductRepository;
import com.senibo.e_commerce_api.repository.UserRepository;
import com.senibo.e_commerce_api.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * Implements the service for managing user shopping carts.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Adds an item to the user's shopping cart or updates its quantity if it already exists.
     *
     * @param userId  The ID of the user.
     * @param request DTO containing the product ID and quantity to add.
     * @return The updated state of the cart as a DTO.
     * @throws NotFoundException          if the user or product is not found.
     * @throws InsufficientStockException if the requested quantity exceeds available stock.
     */
    @Override
    @Transactional
    public CartResponse addItemToCart(UUID userId, AddItemToCartRequest request) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NotFoundException("User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseGet(() -> createNewCartForUser(user));

        Product product = productRepository.findById(request.productId())
                                           .orElseThrow(() -> new NotFoundException(
                                                   "Product not found"));

        if (product.getStockQuantity() < request.quantity()) {
            throw new InsufficientStockException("Not enough stock for product: " + product.getName());
        }

        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                                                 .filter(item -> item.getProduct()
                                                                     .getId()
                                                                     .equals(product.getId()))
                                                 .findFirst();

        if (existingItemOpt.isPresent()) {
            // If item exists, update its quantity
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
        } else {
            // If item is new, create a new CartItem and add it to the cart
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.quantity());
            cart.getCartItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return CartResponse.fromEntity(savedCart);
    }

    /**
     * Retrieves the cart for a specific user.
     *
     * @param userId The ID of the user whose cart is to be retrieved.
     * @return The user's cart as a DTO, or an empty cart DTO if none exists.
     */
    @Override
    public CartResponse getCartForUser(UUID userId) {
        return cartRepository.findByUserId(userId)
                             .map(CartResponse::fromEntity)
                             .orElseGet(() -> new CartResponse(null,
                                                               Collections.emptyList(),
                                                               0,
                                                               BigDecimal.ZERO));
    }

    /**
     * Removes a specific item from a user's cart.
     *
     * @param userId The ID of the user.
     * @param itemId The ID of the cart item to be removed.
     * @return The updated state of the cart as a DTO.
     * @throws NotFoundException if the cart or the item within the cart is not found.
     */
    @Override
    @Transactional
    public CartResponse removeItemFromCart(UUID userId, UUID itemId) {
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new NotFoundException(
                                          "Cart not found for user."));

        CartItem itemToRemove = cart.getCartItems().stream()
                                    .filter(item -> item.getId().equals(itemId))
                                    .findFirst()
                                    .orElseThrow(() -> new NotFoundException(
                                            "Cart item not found in your cart."));

        log.info("User '{}' removing cart item '{}' (Product: {})",
                 userId, itemId, itemToRemove.getProduct().getName());

        cart.getCartItems().remove(itemToRemove);

        Cart updatedCart = cartRepository.save(cart);

        return CartResponse.fromEntity(updatedCart);
    }

    /**
     * A private helper method to create and persist a new, empty cart for a user.
     *
     * @param user The user for whom to create the cart.
     * @return The newly created Cart entity.
     */
    private Cart createNewCartForUser(User user) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }
}
