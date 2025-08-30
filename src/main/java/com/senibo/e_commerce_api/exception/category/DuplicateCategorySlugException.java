package com.senibo.e_commerce_api.exception.category;

import com.senibo.e_commerce_api.exception.ECommerceException;

public class DuplicateCategorySlugException extends ECommerceException {
    public DuplicateCategorySlugException(String message) {
        super(message);
    }
}
