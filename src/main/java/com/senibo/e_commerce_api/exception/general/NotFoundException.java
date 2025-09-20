package com.senibo.e_commerce_api.exception.general;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
