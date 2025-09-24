package com.senibo.e_commerce_api.dto.cart;

import com.senibo.e_commerce_api.model.cart.Cart;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * This record represents the entire shopping cart.
 * Its factory method will be responsible for creating the list of CartItemResponse objects and calculating the final totals.
 */
@Schema(description = "Represents the user's complete shopping cart.")
public record CartResponse(
        @Schema(description = "The unique ID of the cart.")
        UUID id,
        @Schema(description = "A list of all items currently in the cart.")
        List<CartItemResponse> items,
        @Schema(description = "The total number of individual items in the cart.", example = "3")
        int totalItems,
        @Schema(description = "The calculated subtotal for all items in the cart.",
                example = "108.99")
        BigDecimal subtotal
) {
    /**
     * Factory method to create a DTO from a Cart entity.
     * This method handles all calculations.
     */
    public static CartResponse fromEntity(Cart cart) {
        // 1. Convert each CartItem entity into a CartItemResponse DTO
        List<CartItemResponse> cartItems = cart.getCartItems()
                                               .stream()
                                               .map(CartItemResponse::fromEntity)
                                               .toList();

        // 2. Calculate the subtotal by summing up the lineTotal of each item
        BigDecimal subtotal = cartItems.stream()
                                       .map(CartItemResponse::lineTotal)
                                       .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Calculate the total number of items by summing up the quantity of each item
        int totalItems = cartItems.stream()
                                  .mapToInt(CartItemResponse::quantity)
                                  .sum();

        return new CartResponse(
                cart.getId(),
                cartItems,
                totalItems,
                subtotal
        );
    }
}
