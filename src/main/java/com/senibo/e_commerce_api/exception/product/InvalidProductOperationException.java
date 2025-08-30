package com.senibo.e_commerce_api.exception.product;

import com.senibo.e_commerce_api.exception.ECommerceException;

public class InvalidProductOperationException extends ECommerceException {
    public InvalidProductOperationException(String message) {
        super(message);
    }
}
