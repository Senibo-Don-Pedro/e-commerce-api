// Create this in your exception/general package
package com.senibo.e_commerce_api.exception.general;

/**
 * Thrown when a user attempts an operation that is not valid
 * under the current state, e.g., checking out with an empty cart.
 */
public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
