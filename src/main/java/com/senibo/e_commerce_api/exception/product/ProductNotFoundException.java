package com.senibo.e_commerce_api.exception.product;

import com.senibo.e_commerce_api.exception.ECommerceException;

public class ProductNotFoundException extends ECommerceException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
