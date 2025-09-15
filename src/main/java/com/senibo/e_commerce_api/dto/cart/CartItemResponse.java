package com.senibo.e_commerce_api.dto.cart;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(
    UUID id,
    UUID productId,
    String productName,
    String productSku,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal subtotal,
    String productImageUrl,
    Integer availableStock  // So frontend can show stock limits
) {}
