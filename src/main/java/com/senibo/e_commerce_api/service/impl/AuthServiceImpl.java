package com.senibo.e_commerce_api.service.impl;

import com.senibo.e_commerce_api.dto.auth.AuthResponse;
import com.senibo.e_commerce_api.dto.auth.SigninRequest;
import com.senibo.e_commerce_api.dto.auth.SignupRequest;
import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.repository.UserRepository;
import com.senibo.e_commerce_api.security.jwt.JwtUtils;
import com.senibo.e_commerce_api.security.services.UserDetailsImpl;
import com.senibo.e_commerce_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository users;
    private final JwtUtils jwt;

    @Transactional
    @Override
    public void register(SignupRequest req) {
        if (users.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username taken");
        }

        if (users.existsByEmail(req.email())) {
            throw new IllegalArgumentException(String.format("Email %s in use", req.email()));
        }

        User user = User.builder()
                        .username(req.username())
                        .email(req.email())
                        .firstname(req.firstname())
                        .lastname(req.lastname())
                        .password(passwordEncoder.encode(req.password()))
                        .build();

        users.save(user);

        log.info("Signup success userId={} username={}", user.getId(), user.getUsername());
    }

    @Override
    public AuthResponse login(SigninRequest req) {
        var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.identifier(),
                                                                                    req.password()));

        var principal = (UserDetailsImpl) auth.getPrincipal();
        var token = jwt.generate(principal);
        var roles = principal.getAuthorities()
                             .stream()
                             .map(GrantedAuthority::getAuthority)
                             .toList();

        log.info("Login success user={} roles={}", principal.getUsername(), roles);

        return new AuthResponse(principal.getUsername(),
                                principal.getEmail(),
                                principal.getFirstName(),
                                principal.getLastName(),
                                token,
                                roles);

    }
}
