package com.senibo.e_commerce_api.config;

import com.senibo.e_commerce_api.exception.general.NotFoundException;
import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.repository.UserRepository;
import com.senibo.e_commerce_api.security.jwt.JwtUtils;
import com.senibo.e_commerce_api.security.services.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handles the logic to be executed upon successful OAuth2 authentication.
 * <p>
 * After a user logs in via an external provider (e.g., Google), this handler
 * is responsible for generating a JWT for the user and redirecting them to the
 * frontend application with the token.
 */
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtService;
    private final UserRepository userRepository;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * Called when a user has been successfully authenticated via OAuth2.
     * <p>
     * The process is as follows:
     * 1. Extracts the user's email from the OAuth2 principal.
     * 2. Fetches the corresponding User entity from the application's database.
     * 3. Generates a JWT for the authenticated user.
     * 4. Redirects the user to a predefined frontend URL, passing the JWT as a query parameter.
     *
     * @param request        the HTTP request.
     * @param response       the HTTP response.
     * @param authentication the Authentication object which contains the principal.
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        User user = userRepository.findByEmail(email)
                                  .orElseThrow(() -> new NotFoundException(
                                          "User not found in DB after OAuth login."));

        String jwtToken = jwtService.generate(UserDetailsImpl.from(user));

        // The frontend will be responsible for parsing the token from the URL
        // and storing it (e.g., in localStorage).
        String targetUrl = "https://e-commerce-frontend-seven-silk.vercel.app/oauth-redirect?token=" + jwtToken;

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}
