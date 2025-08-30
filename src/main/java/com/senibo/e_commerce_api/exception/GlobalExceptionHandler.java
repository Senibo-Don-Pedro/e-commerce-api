package com.senibo.e_commerce_api.exception;

import com.senibo.e_commerce_api.dto.ApiResponseDto;
import com.senibo.e_commerce_api.exception.category.CategoryDeletionException;
import com.senibo.e_commerce_api.exception.category.CategoryNotFoundException;
import com.senibo.e_commerce_api.exception.category.DuplicateCategorySlugException;
import com.senibo.e_commerce_api.exception.product.DuplicateProductSkuException;
import com.senibo.e_commerce_api.exception.product.InvalidProductOperationException;
import com.senibo.e_commerce_api.exception.product.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // **THIS IS THE KEY HANDLER FOR YOUR 403 ISSUE**
    // Handles malformed JSON requests (like your missing closing brace)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDto<?>> handleInvalidJson(HttpMessageNotReadableException ex) {
        log.debug("Invalid JSON received: {}", ex.getMessage());

        String errorMessage = "Invalid JSON format";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ApiResponseDto.error(ex.getMessage()));
    }


    // DTO @Valid errors (e.g., empty JSON on /signup)
    // 400 - Bad Request (Validation errors)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<?>> handleDtoValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(fe -> fe.getField() + ": " + (fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage()))
                                .toList();
        log.debug("Validation failed: {}", errors);
        // If your ApiResponse supports a list:
        return ResponseEntity.badRequest()
                             .body(ApiResponseDto.error("Validation failed for the " + "following reasons:",
                                                        errors));
    }

    // Handle Spring Security authentication failures
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiResponseDto<?>> handleAuthenticationException(org.springframework.security.core.AuthenticationException ex) {
        log.debug("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(ApiResponseDto.error("Invalid credentials"));
    }


    // Your service throws these for “username taken”, “email in use”, etc.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<?>> handleIllegalArg(IllegalArgumentException ex) {
        log.debug("Bad request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ApiResponseDto.error(ex.getMessage()));
    }


    // Handle 404 - Not Found Errors
    @ExceptionHandler({ProductNotFoundException.class, CategoryNotFoundException.class})
    public ResponseEntity<ApiResponseDto<?>> handleProductNotFound(ECommerceException ex) {
        log.error("Product not found: {}", ex.getMessage());
        ApiResponseDto<?> error = ApiResponseDto.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Handle 409 - Conflict Errors
    @ExceptionHandler({
            DuplicateCategorySlugException.class,
            DuplicateProductSkuException.class,
            CategoryDeletionException.class,
            InvalidProductOperationException.class
    })
    public ResponseEntity<ApiResponseDto<?>> handleConflict(ECommerceException ex) {
        log.error("Business logic conflict: {}", ex.getMessage());
        ApiResponseDto<?> error = ApiResponseDto.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }


    // Handles access denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDto<?>> handleIllegalArg(AccessDeniedException ex) {
        log.debug("Access Denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(ApiResponseDto.error(ex.getMessage()));
    }

    // Catch-all for any unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<?>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(ApiResponseDto.error(String.format(
                                     "An unexpected error occurred, %s",
                                     ex.getMessage())));
    }
}
