package com.senibo.e_commerce_api.exception;

import com.senibo.e_commerce_api.dto.ApiErrorResponse;
import com.senibo.e_commerce_api.exception.auth.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handler for your custom "User already exists" exception
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
    public ApiErrorResponse handleAuthenticationException(AuthenticationException ex) {
        return new ApiErrorResponse(
                false,
                ex.getMessage(), // Use the message from your exception
                null // No detailed error list needed for this one
        );
    }

    // Handler for validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
    public ApiErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Collect all the error messages from the validation annotations
        List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .collect(Collectors.toList());

        return new ApiErrorResponse(
                false,
                "Validation Failed",
                errors
        );
    }

    // Add handler for authentication/authorization errors (for login)
    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
    public ApiErrorResponse handleBadCredentialsException(org.springframework.security.authentication.BadCredentialsException ex) {
        return new ApiErrorResponse(
                false,
                "Invalid email or password",
                null
        );
    }

    // Add handler for user not found (for login)
    @ExceptionHandler(org.springframework.security.core.userdetails.UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
    public ApiErrorResponse handleUsernameNotFoundException(org.springframework.security.core.userdetails.UsernameNotFoundException ex) {
        return new ApiErrorResponse(
                false,
                "User not found",
                null
        );
    }

    // Generic catch-all handler for any other unexpected exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
    public ApiErrorResponse handleGenericException(Exception ex) {
        // It's a good practice to log the full exception for debugging purposes
        log.error("An unexpected error occurred", ex);
        return new ApiErrorResponse(
                false,
                "An unexpected internal server error occurred.", // A generic, safe message
                null
        );
    }
}
