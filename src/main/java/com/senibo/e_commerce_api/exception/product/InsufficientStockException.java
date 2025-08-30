package com.senibo.e_commerce_api.exception.product;

import com.senibo.e_commerce_api.exception.ECommerceException;

public class InsufficientStockException extends ECommerceException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
