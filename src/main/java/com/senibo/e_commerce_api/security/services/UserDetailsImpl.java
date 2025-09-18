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

    public static UserDetailsImpl from(User u) {
        // Reorder these arguments to match the constructor
        return new UserDetailsImpl(
                u.getId(),
                u.getFirstname(), // Correct order
                u.getLastname(),  // Correct order
                u.getUsername(),  // Correct order
                u.getEmail(),     // Correct order
                u.getPassword(),
                List.of(new SimpleGrantedAuthority(u.getRole().name()))
        );
    }

    public boolean isAccountNonExpired(){ return true; }
    public boolean isAccountNonLocked(){ return true; }
    public boolean isCredentialsNonExpired(){ return true; }
    public boolean isEnabled(){ return true; }
}
