package com.senibo.e_commerce_api.service.impl;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.PagedResult;
import com.senibo.e_commerce_api.dto.order.OrderDTO;
import com.senibo.e_commerce_api.exception.general.InvalidOperationException;
import com.senibo.e_commerce_api.exception.general.NotFoundException;
import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.model.cart.Cart;
import com.senibo.e_commerce_api.model.order.Order;
import com.senibo.e_commerce_api.model.order.OrderStatus;
import com.senibo.e_commerce_api.model.orderItem.OrderItem;
import com.senibo.e_commerce_api.repository.CartRepository;
import com.senibo.e_commerce_api.repository.OrderRepository;
import com.senibo.e_commerce_api.repository.UserRepository;
import com.senibo.e_commerce_api.service.OrderService;
import com.senibo.e_commerce_api.util.OrderSpecification;
import com.senibo.e_commerce_api.util.PaginationValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final PaginationValidator paginationValidator;

    /**
     * Creates a permanent order from a user's current shopping cart.
     * This operation is transactional: it succeeds completely or not at all.
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

        return orderRepository.save(order);
    }


    /**
     * Retrieves a paginated and filterable list of orders for a specific user.
     * This method ensures that a user can only access their own order history.
     *
     * @param userId        The ID of the user whose orders are being requested. This is a security boundary.
     * @param page          The page number to retrieve (0-indexed).
     * @param pageSize      The number of orders to include per page.
     * @param sortBy        The field to sort the orders by (e.g., "createdAt").
     * @param sortDirection The direction of the sort ("asc" or "desc").
     * @param status        An optional filter to find orders with a specific {@link OrderStatus}.
     * @return An {@link ApiSuccessResponse} containing a {@link PagedResult} of {@link OrderDTO}s.
     * @throws NotFoundException if the user with the given ID is not found.
     */
    @Override
    public ApiSuccessResponse<PagedResult<OrderDTO>> getOrdersForUser(
            UUID userId,
            int page,
            int pageSize,
            String sortBy,
            String sortDirection,
            Optional<OrderStatus> status) {
        // 1. Find the user to ensure the request is valid and for security.
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NotFoundException("User not found"));

        // 2. Create a Pageable object for pagination and sorting.
        Pageable pageable = paginationValidator.createPageable(page,
                                                               pageSize,
                                                               sortBy,
                                                               sortDirection);

        // 3. Build the dynamic query using specifications. This always includes the user check.
        Specification<Order> spec = OrderSpecification.build(user, status);

        // 4. Execute the query against the database.
        Page<Order> orderPage = orderRepository.findAll(spec, pageable);

        // 5. Map the Page of database entities to a Page of DTOs.
        Page<OrderDTO> orderDtoPage = orderPage.map(OrderDTO::mapOrderToDto);

        // 6. Convert the Spring Page object into our clean, custom PagedResult.
        PagedResult<OrderDTO> pagedResult = PagedResult.from(orderDtoPage);

        // 7. Wrap the final result in a standard success response.
        return new ApiSuccessResponse<>(true, "Orders retrieved successfully", pagedResult);
    }
}
