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

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    // Ensures all database operations in this method are part of a single transaction
    public CartResponse addItemToCart(UUID userId, AddItemToCartRequest request) {
        // 1. Find the User
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NotFoundException("User not found"));

        // 2. Find the user's cart, or create a new one if it doesn't exist
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseGet(() -> createNewCartForUser(user));

        // 3. Find the product to be added to the cart
        Product product = productRepository.findById(request.productId())
                                           .orElseThrow(() -> new NotFoundException(
                                                   "Product not found"));

        // 4. Check if there is enough stock
        if (product.getStockQuantity() < request.quantity()) {
            throw new InsufficientStockException("Not enough stock available for product: " + product.getName());
        }

        // 5. Check if the item is already in the cart
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

        // 7. Save the cart (changes to CartItems will be cascaded) and return the response
        Cart savedCart = cartRepository.save(cart);
        return CartResponse.fromEntity(savedCart);
    }

    //    public CartResponse getCartForUser(UUID userId) {
    //        // Find the cart by the user's ID
    //        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
    //
    //        if (cartOpt.isPresent()) {
    //            // If the cart exists, convert it to a DTO and return it
    //            return CartResponse.fromEntity(cartOpt.get());
    //        } else {
    //            // If the user has no cart yet, return a new, empty cart response
    //            // This is better than a 404 error, as a user always conceptually has a cart.
    //            return new CartResponse(null, Collections.emptyList(), 0, BigDecimal.ZERO);
    //        }
    //    }
    public CartResponse getCartForUser(UUID userId) {
        // Find the cart by the user's ID
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        // If the cart exists, convert it to a DTO and return it
        // If the user has no cart yet, return a new, empty cart response
        // This is better than a 404 error, as a user always conceptually has a cart.
        return cartOpt.map(CartResponse::fromEntity)
                      .orElseGet(() -> new CartResponse(null,
                                                        Collections.emptyList(),
                                                        0,
                                                        BigDecimal.ZERO));
    }

    @Override
    @Transactional
    public CartResponse removeItemFromCart(UUID userId, UUID itemId) {
        // 1. Find the user's cart
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new NotFoundException(
                                          "Cart not found for user."));

        // 2. Find the specific item within that user's cart
        CartItem itemToRemove = cart.getCartItems().stream()
                                    .filter(item -> item.getId().equals(itemId))
                                    .findFirst()
                                    .orElseThrow(() -> new NotFoundException(
                                            "Cart item not found in your cart."));

        // 3. Log the action for auditing and debugging
        log.info("User '{}' is removing cart item '{}' (Product: {})",
                 userId, itemId, itemToRemove.getProduct().getName());

        // 4. Remove the item from the collection
        cart.getCartItems().remove(itemToRemove);

        // 5. Save the cart
        Cart updatedCart = cartRepository.save(cart);

        // 6. Return the updated state of the cart
        return CartResponse.fromEntity(updatedCart);
    }


    private Cart createNewCartForUser(User user) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }


}
