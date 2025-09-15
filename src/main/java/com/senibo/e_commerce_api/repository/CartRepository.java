package com.senibo.e_commerce_api.repository;

import com.senibo.e_commerce_api.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByUserId(UUID userId);
}
