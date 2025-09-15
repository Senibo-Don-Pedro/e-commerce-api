package com.senibo.e_commerce_api.repository;

import com.senibo.e_commerce_api.model.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);
    List<CartItem> findAllByCartId(UUID cartId);
    void deleteAllByCartId(UUID cartId);
}
