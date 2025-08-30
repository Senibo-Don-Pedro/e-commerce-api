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

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl from(User u) {
        return new UserDetailsImpl(
                u.getId(), u.getEmail(),u.getUsername(), u.getFirstname(), u.getLastname(),
                u.getPassword(),
                List.of(new SimpleGrantedAuthority(u.getRole().name()))
        );
    }

    public boolean isAccountNonExpired(){ return true; }
    public boolean isAccountNonLocked(){ return true; }
    public boolean isCredentialsNonExpired(){ return true; }
    public boolean isEnabled(){ return true; }
}
