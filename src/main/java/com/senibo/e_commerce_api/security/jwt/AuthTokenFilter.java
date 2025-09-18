package com.senibo.e_commerce_api.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senibo.e_commerce_api.dto.ApiErrorResponse;
import com.senibo.e_commerce_api.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwt;
    private final UserDetailsServiceImpl uds;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {

            log.debug("Bearer token present on {}", req.getRequestURI());
            String token = auth.substring(7);

            // First check if token is valid using your existing method
            if (jwt.isValid(token)) {
                try {
                    // If valid, try to extract username and authenticate
                    String username = jwt.getUsername(token);
                    var userDetails = uds.loadUserByUsername(username);
                    var authToken = new UsernamePasswordAuthenticationToken(userDetails,
                                                                            null,
                                                                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.debug("Authentication successful for user: {}", username);

                } catch (Exception ex) {
                    // This shouldn't happen if jwt.isValid() returned true, but just in case
                    log.error("Unexpected error during authentication for request: {}",
                              req.getRequestURI(),
                              ex);
                    sendErrorResponse(res, HttpStatus.UNAUTHORIZED, "Authentication failed");
                    return;
                }
            } else {
                // Token is invalid - jwt.isValid() already logged the specific reason
                log.debug("Invalid token for request: {}", req.getRequestURI());
                sendErrorResponse(res, HttpStatus.UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        }

        // Continue with the filter chain
        chain.doFilter(req, res);
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   HttpStatus status,
                                   String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiErrorResponse errorResponse = new ApiErrorResponse(false, message, null);

        objectMapper.writeValue(response.getOutputStream(), errorResponse);

        log.debug("Sent error response: {} - {}", status.value(), message);
    }
}
