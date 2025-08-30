package com.senibo.e_commerce_api.exception.category;

import com.senibo.e_commerce_api.exception.ECommerceException;

public class CategoryNotFoundException extends ECommerceException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
