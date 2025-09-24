package com.senibo.e_commerce_api.service;

import com.senibo.e_commerce_api.model.order.Order;

import java.util.UUID;


public interface OrderService {

    Order createOrderFromCart(UUID userId);
}
