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

/**
 * A custom filter that executes once per request to handle JWT-based authentication.
 * <p>
 * This filter inspects the "Authorization" header for a "Bearer" token. If a valid
 * token is found, it authenticates the user and sets the security context for the
 * duration of the request.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwt;
    private final UserDetailsServiceImpl uds;
    private final ObjectMapper objectMapper;

    /**
     * Performs the JWT validation and authentication logic for each request.
     *
     * @param req   The incoming HTTP request.
     * @param res   The outgoing HTTP response.
     * @param chain The filter chain.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {

            log.debug("Bearer token present on {}", req.getRequestURI());
            String token = auth.substring(7);

            if (jwt.isValid(token)) {
                try {
                    // If token is valid, extract username and set authentication context
                    String username = jwt.getUsername(token);
                    var userDetails = uds.loadUserByUsername(username);
                    var authToken = new UsernamePasswordAuthenticationToken(userDetails,
                                                                            null,
                                                                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authentication successful for user: {}", username);
                } catch (Exception ex) {
                    log.error("Error during authentication for request: {}",
                              req.getRequestURI(),
                              ex);
                    sendErrorResponse(res, HttpStatus.UNAUTHORIZED, "Authentication failed");
                    return;
                }
            } else {
                // If token is invalid, send an error response
                log.debug("Invalid token for request: {}", req.getRequestURI());
                sendErrorResponse(res, HttpStatus.UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        }

        // Continue with the rest of the filter chain
        chain.doFilter(req, res);
    }

    /**
     * Writes a standardized JSON error response to the client.
     *
     * @param response The HTTP response object.
     * @param status   The HTTP status to set.
     * @param message  The error message.
     */
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
