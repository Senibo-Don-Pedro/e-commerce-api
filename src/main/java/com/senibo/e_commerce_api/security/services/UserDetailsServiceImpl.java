package com.senibo.e_commerce_api.security.services;

import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * An implementation of Spring Security's {@link UserDetailsService}.
 * Its primary role is to load a user's data from the database given a username or email
 * and wrap it in a {@link UserDetails} object for the authentication process.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository users;

    /**
     * Locates the user based on the username or email.
     *
     * @param usernameOrEmail The username or email identifying the user whose data is required.
     * @return a fully populated user record (never {@code null}).
     * @throws UsernameNotFoundException if the user could not be found.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = users.findByEmailOrUsername(usernameOrEmail, usernameOrEmail)
                         .orElseThrow(() -> new UsernameNotFoundException(
                                 "User not found with username or email: " + usernameOrEmail));

        return UserDetailsImpl.from(user);
    }
}
