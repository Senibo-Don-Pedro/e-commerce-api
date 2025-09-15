package com.senibo.e_commerce_api.dto.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CartResponse(
    UUID id,
    List<CartItemResponse> items,
    Integer totalItems,
    BigDecimal totalAmount,
    LocalDateTime lastUpdated
) {}
