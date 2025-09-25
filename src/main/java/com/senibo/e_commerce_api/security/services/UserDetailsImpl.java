package com.senibo.e_commerce_api.security.services;

import com.senibo.e_commerce_api.model.auth.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * An implementation of Spring Security's {@link UserDetails} interface.
 * This class acts as a wrapper around the application's {@link User} entity,
 * providing core user information in a format that Spring Security can understand.
 */
@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * A static factory method to create a {@link UserDetailsImpl} instance from a {@link User} entity.
     *
     * @param u The User entity from the database.
     * @return A new UserDetailsImpl object.
     */
    public static UserDetailsImpl from(User u) {
        return new UserDetailsImpl(
                u.getId(),
                u.getFirstname(),
                u.getLastname(),
                u.getUsername(),
                u.getEmail(),
                u.getPassword(),
                List.of(new SimpleGrantedAuthority(u.getRole().name()))
        );
    }

    // --- UserDetails contract methods ---

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
