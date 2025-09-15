package com.senibo.e_commerce_api.dto.cart;

import java.math.BigDecimal;

public record CartOperationResponse(
    String message,
    Integer totalItems,
    BigDecimal totalAmount
) {}
