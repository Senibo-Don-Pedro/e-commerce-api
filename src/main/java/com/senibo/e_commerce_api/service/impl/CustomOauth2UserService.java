package com.senibo.e_commerce_api.service.impl;

import com.senibo.e_commerce_api.model.auth.Provider;
import com.senibo.e_commerce_api.model.auth.Role;
import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom implementation of {@link DefaultOAuth2UserService} to process users
 * authenticated via an OAuth2 provider (e.g., Google).
 * <p>
 * This service handles linking an OAuth2 login to an existing user account
 * (based on email) or registering a new user if no account exists.
 */
@Slf4j
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user's details after a successful OAuth2 authentication.
     *
     * @param userRequest The OAuth2 user request.
     * @return An {@link OAuth2User} containing the user's attributes.
     * @throws OAuth2AuthenticationException if an error occurs during processing.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);
        String email = oauthUser.getAttribute("email");

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        if (userOptional.isPresent()) {
            // --- ACCOUNT LINKING ---
            // User exists, log them into their existing account.
            user = userOptional.get();
            log.info("User found with email {}", email);
            user.setFirstname(oauthUser.getAttribute("given_name"));
            user.setLastname(oauthUser.getAttribute("family_name"));
        } else {
            // --- NEW USER REGISTRATION ---
            // User does not exist, create a new record.
            log.info("New user detected. Creating account for: {}", email);
            user = new User();
            user.setEmail(email);
            user.setFirstname(oauthUser.getAttribute("given_name"));
            user.setLastname(oauthUser.getAttribute("family_name"));
            user.setUsername(email); // Using email as username since it's required and unique
            user.setProvider(Provider.GOOGLE); // Mark that this user signed up via Google
            user.setRole(Role.CUSTOMER); // Assign a default role
        }

        userRepository.save(user);
        return oauthUser;
    }
}
