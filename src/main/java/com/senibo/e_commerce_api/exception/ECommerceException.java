package com.senibo.e_commerce_api.exception;

public class ECommerceException extends RuntimeException {
    public ECommerceException(String message) {
        super(message);
    }

    public ECommerceException(String message, Throwable cause) {
        super(message, cause);
    }
}
