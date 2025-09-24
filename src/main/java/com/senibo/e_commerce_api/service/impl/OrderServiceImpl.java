package com.senibo.e_commerce_api.service.impl;

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

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional // This is important to ensure all operations succeed or fail together
    public Order createOrderFromCart(UUID userId) {

        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NotFoundException("User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new NotFoundException("Cart is empty"));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order from an empty cart."); // Switched to a more fitting exception
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

        // Calculate total amount directly from the new orderItems list
        BigDecimal totalAmount = orderItems.stream()
                                           .map(item -> item.getPricePerUnit()
                                                            .multiply(BigDecimal.valueOf(item.getQuantity())))
                                           .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        // 1. Save the newly created order
        Order savedOrder = orderRepository.save(order);

        // 2. Clear the items from the user's cart
        cart.getCartItems().clear();

        // 3. Save the updated (now empty) cart
        cartRepository.save(cart);

        // 4. Return the order that was created
        return savedOrder;
    }
}
