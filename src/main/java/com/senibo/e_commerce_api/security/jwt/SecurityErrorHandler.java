package com.senibo.e_commerce_api.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senibo.e_commerce_api.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A centralized handler for security-related exceptions.
 * Implements {@link AuthenticationEntryPoint} to handle 401 Unauthorized errors
 * and {@link AccessDeniedHandler} to handle 403 Forbidden errors, providing
 * consistent JSON error responses for both.
 */
@Component
@RequiredArgsConstructor
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * A helper method to write a standardized JSON error response to the client.
     */
    private void write(HttpServletResponse res, int status, String message) throws IOException {
        res.setStatus(status);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(res.getOutputStream(), new ApiErrorResponse(false, message, null));
    }

    /**
     * Handles authentication failures (e.g., bad/missing/expired token), returning a 401 Unauthorized status.
     */
    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse res,
                         AuthenticationException ex) throws IOException {
        write(res, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + ex.getMessage());
    }

    /**
     * Handles authorization failures (e.g., authenticated user lacks required roles), returning a 403 Forbidden status.
     */
    @Override
    public void handle(HttpServletRequest req,
                       HttpServletResponse res,
                       AccessDeniedException ex) throws IOException {
        write(res, HttpServletResponse.SC_FORBIDDEN, "Forbidden: " + ex.getMessage());
    }
}
