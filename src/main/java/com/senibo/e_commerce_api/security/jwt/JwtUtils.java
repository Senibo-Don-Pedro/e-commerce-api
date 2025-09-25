package com.senibo.e_commerce_api.security.jwt;

import com.senibo.e_commerce_api.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * A utility component for handling JSON Web Tokens (JWTs).
 * Provides methods to generate, validate, and parse tokens.
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${spring.app.jwtSecret}")
    private String base64Secret;

    @Value("${spring.app.jwtExpirationMs}")
    private long expiresMs;

    /**
     * Creates the signing key from the base64 encoded secret.
     *
     * @return The HMAC-SHA secret key.
     */
    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    }

    /**
     * Generates a JWT for a given authenticated user.
     *
     * @param user The user details object.
     * @return A signed JWT string.
     */
    public String generate(UserDetailsImpl user) {
        String roles = user.getAuthorities()
                           .stream()
                           .map(GrantedAuthority::getAuthority)
                           .collect(Collectors.joining(","));

        return Jwts.builder()
                   .subject(user.getUsername())
                   .claim("uid", user.getId())
                   .claim("roles", roles)
                   .issuedAt(new Date())
                   .expiration(new Date(System.currentTimeMillis() + expiresMs))
                   .signWith(key())
                   .compact();
    }

    /**
     * Extracts the username (subject) from a given JWT.
     *
     * @param token The JWT string.
     * @return The username.
     */
    public String getUsername(String token) {
        return Jwts.parser()
                   .verifyWith(key())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getSubject();
    }

    /**
     * Validates a JWT's signature and expiration.
     *
     * @param token The JWT string to validate.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
        } catch (JwtException e) { // Covers malformed, unsupported, signature errors
            log.warn("JWT invalid: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty or null: {}", e.getMessage());
        }
        return false;
    }
}
