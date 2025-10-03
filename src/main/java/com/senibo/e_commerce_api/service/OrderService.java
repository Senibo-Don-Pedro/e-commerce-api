package com.senibo.e_commerce_api.service;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.PagedResult;
import com.senibo.e_commerce_api.dto.order.OrderDTO;
import com.senibo.e_commerce_api.model.order.Order;
import com.senibo.e_commerce_api.model.order.OrderStatus;

import java.util.Optional;
import java.util.UUID;


public interface OrderService {

    /**
     * Creates an order from the cart.
     *
     * @param userId The ID of the user.
     * @return A list of orders.
     */
    Order createOrderFromCart(UUID userId);


    /**
     * Retrieves the order history for a specific user.
     *
     * @param userId The ID of the user whose orders are to be retrieved.
     * @return A list of orders, formatted as DTOs.
     */
    ApiSuccessResponse<PagedResult<OrderDTO>> getOrdersForUser(
            UUID userId,
            int page,
            int pageSize,
            String sortBy,
            String sortDirection,
            Optional<OrderStatus> status
    );
}
