package com.senibo.e_commerce_api.exception;

import com.senibo.e_commerce_api.dto.ApiErrorResponse;
import com.senibo.e_commerce_api.exception.auth.AuthenticationException;
import com.senibo.e_commerce_api.exception.general.InsufficientStockException;
import com.senibo.e_commerce_api.exception.general.InvalidOperationException;
import com.senibo.e_commerce_api.exception.general.NotFoundException;
import com.senibo.e_commerce_api.exception.general.ResourceConflictException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for the REST API.
 * This class uses @RestControllerAdvice to provide centralized exception handling
 * across all @RestController classes. It converts thrown exceptions into a
 * consistent JSON format defined by {@link ApiErrorResponse}.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =======================================================================================
    // == 400 BAD REQUEST ====================================================================
    // =======================================================================================

    /**
     * Handles validation errors triggered by the @Valid annotation on request bodies.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .collect(Collectors.toList());
        return new ApiErrorResponse(false, "Validation Failed", errors);
    }

    /**
     * Handles errors that occur when the request body is malformed or contains invalid JSON.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleJsonParseException(HttpMessageNotReadableException ex) {
        log.warn("Request JSON could not be parsed: {}", ex.getMessage());
        return new ApiErrorResponse(false,
                                    "Invalid request format. Please check the JSON body for errors.",
                                    null);
    }

    /**
     * Handles attempts to perform an operation that is invalid in the current state (e.g., checkout with empty cart).
     */
    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidOperationException(InvalidOperationException ex) {
        return new ApiErrorResponse(false, ex.getMessage(), null);
    }


    // =======================================================================================
    // == 401 UNAUTHORIZED ===================================================================
    // =======================================================================================

    /**
     * Handles authentication failures due to incorrect credentials during login.
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleBadCredentialsException(BadCredentialsException ex) {
        return new ApiErrorResponse(false, "Invalid email or password", null);
    }

    // =======================================================================================
    // == 403 FORBIDDEN ======================================================================
    // =======================================================================================

    /**
     * Handles authorization failures where a user is authenticated but lacks the necessary permissions.
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return new ApiErrorResponse(false,
                                    "You do not have the required permissions to perform this action.",
                                    null);
    }

    // =======================================================================================
    // == 404 NOT FOUND ======================================================================
    // =======================================================================================

    /**
     * Handles cases where a requested resource could not be found.
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFoundException(NotFoundException ex) {
        return new ApiErrorResponse(false, ex.getMessage(), null);
    }

    // =======================================================================================
    // == 409 CONFLICT =======================================================================
    // =======================================================================================

    /**
     * Handles conflicts during user registration, such as a username or email already being in use.
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleAuthenticationException(AuthenticationException ex) {
        return new ApiErrorResponse(false, ex.getMessage(), null);
    }

    /**
     * Handles general resource conflicts, such as trying to create a product that already exists.
     */
    @ExceptionHandler(ResourceConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleResourceConflictException(ResourceConflictException ex) {
        return new ApiErrorResponse(false, ex.getMessage(), null);
    }

    /**
     * Handles business rule conflicts, such as insufficient product stock.
     */
    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleInsufficientStockException(InsufficientStockException ex) {
        return new ApiErrorResponse(false, ex.getMessage(), null);
    }

    /**
     * Handles exceptions related to database integrity constraints (e.g., UNIQUE violations).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Database integrity constraint violated: {}", ex.getMessage());
        return new ApiErrorResponse(false,
                                    "A database constraint was violated. Please check your data.",
                                    null);
    }


    // =======================================================================================
    // == 415 UNSUPPORTED MEDIA TYPE =========================================================
    // =======================================================================================

    /**
     * Handles errors when the client sends an unsupported Content-Type header.
     * This is the correct, specific status code for this type of client error.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE) // Use the 415 status code
    public ApiErrorResponse handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex) {
        log.warn(
                "Unsupported Content-Type received: {}. Supported types are likely application/json.",
                ex.getContentType());
        return new ApiErrorResponse(false,
                                    "The content type of your request is not supported. Please use 'application/json'.",
                                    null);
    }

    // =======================================================================================
    // == 500 INTERNAL SERVER ERROR ==========================================================
    // =======================================================================================

    /**
     * A generic catch-all handler for any other unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleGenericException(Exception ex) {
        log.error("An unexpected error occurred : ", ex);
        return new ApiErrorResponse(false, "An unexpected internal server error occurred.", null);
    }
}
