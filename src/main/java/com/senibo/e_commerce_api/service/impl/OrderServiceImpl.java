package com.senibo.e_commerce_api.service.impl;

import com.senibo.e_commerce_api.exception.general.InvalidOperationException;
import com.senibo.e_commerce_api.exception.general.NotFoundException;
import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.model.cart.Cart;
import com.senibo.e_commerce_api.model.order.Order;
import com.senibo.e_commerce_api.model.orderItem.OrderItem;
import com.senibo.e_commerce_api.repository.CartRepository;
import com.senibo.e_commerce_api.repository.OrderRepository;
import com.senibo.e_commerce_api.repository.UserRepository;
import com.senibo.e_commerce_api.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implements the service for managing customer orders.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    /**
     * Creates a permanent order from a user's current shopping cart.
     * This operation is transactional: it succeeds completely or not at all.
     * Upon success, the user's cart is cleared.
     *
     * @param userId The ID of the user placing the order.
     * @return The newly created Order entity.
     * @throws NotFoundException         if the user or their cart is not found.
     * @throws InvalidOperationException if the cart is empty.
     */
    @Override
    @Transactional
    public Order createOrderFromCart(UUID userId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NotFoundException("User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new NotFoundException("Cart is empty"));

        if (cart.getCartItems().isEmpty()) {
            throw new InvalidOperationException("Cannot create an order from an empty cart.");
        }

        Order order = new Order();
        order.setUser(user);

        // Convert CartItems to OrderItems and link them to the new Order
        List<OrderItem> orderItems = cart.getCartItems().stream()
                                         .map(cartItem -> {
                                             OrderItem orderItem = new OrderItem();
                                             orderItem.setOrder(order);
                                             orderItem.setProduct(cartItem.getProduct());
                                             orderItem.setQuantity(cartItem.getQuantity());
                                             orderItem.setPricePerUnit(cartItem.getProduct()
                                                                               .getPrice());
                                             return orderItem;
                                         }).collect(Collectors.toCollection(ArrayList::new));

        order.setOrderItems(orderItems);

        // Calculate total amount
        BigDecimal totalAmount = orderItems.stream()
                                           .map(item -> item.getPricePerUnit()
                                                            .multiply(BigDecimal.valueOf(item.getQuantity())))
                                           .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // --- THIS IS THE CHANGE ---
        // We NO LONGER clear the cart here.
        // cart.getCartItems().clear();
        // cartRepository.save(cart);
        // ------------------------

        return savedOrder;
    }
}
