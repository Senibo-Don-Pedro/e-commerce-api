package com.senibo.e_commerce_api.dto.cart;

import com.senibo.e_commerce_api.model.cartItem.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;


/**
 * This record will represent a single line item in the cart.
 * It will pull information from both the CartItem entity and its related Product entity.
 */

@Schema(description = "Represents a single item within a shopping cart.")
public record CartItemResponse(
        @Schema(description = "The unique ID of this specific cart item.")
        UUID itemId,
        @Schema(description = "The ID of the product.")
        UUID productId,
        @Schema(description = "The name of the product.", example = "Classic Leather Wallet")
        String productName,
        @Schema(description = "The image URL for the product.")
        String imageUrl,
        @Schema(description = "The price of a single unit of the product.", example = "49.99")
        BigDecimal unitPrice,
        @Schema(description = "The quantity of this item in the cart.", example = "1")
        int quantity,
        @Schema(description = "The calculated total price for this line item (unitPrice * quantity).",
                example = "49.99")
        BigDecimal lineTotal
) {

    /**
     * Factory method to create a DTO from a CartItem entity.
     */
    public static CartItemResponse fromEntity(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getImageUrl(),
                cartItem.getProduct().getPrice(),
                cartItem.getQuantity(),
                //Calculate total price
                cartItem.getProduct()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
        );
    }
}
