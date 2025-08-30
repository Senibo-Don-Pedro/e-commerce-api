package com.senibo.e_commerce_api.exception.category;

import com.senibo.e_commerce_api.exception.ECommerceException;

public class CategoryDeletionException extends ECommerceException {
    public CategoryDeletionException(String message) {
        super(message);
    }
}
