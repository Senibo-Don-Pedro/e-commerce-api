package com.senibo.e_commerce_api.exception.product;

import com.senibo.e_commerce_api.exception.ECommerceException;

public class DuplicateProductSkuException extends ECommerceException {
    public DuplicateProductSkuException(String message) {
        super(message);
    }
}
