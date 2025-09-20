package com.senibo.e_commerce_api.service.impl;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.auth.LoginRequest;
import com.senibo.e_commerce_api.dto.auth.LoginResponse;
import com.senibo.e_commerce_api.dto.auth.RegisterRequest;
import com.senibo.e_commerce_api.exception.auth.AuthenticationException;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwt;

    @Override
    public ApiSuccessResponse<Object> registerUser(RegisterRequest registerRequest) {
        // Step 1: Handle the "failure" case
        if (userRepository.existsByUsername(registerRequest.username())) {
            // Later, a global handler will catch this and create your ApiErrorResponse
            throw new AuthenticationException("Username is already taken.");
        }

        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new AuthenticationException("Email is already in use.");
        }

        User newUser = User.builder()
                           .firstname(registerRequest.firstname())
                           .lastname(registerRequest.lastname())
                           .username(registerRequest.username())
                           .email(registerRequest.email())
                           .password(passwordEncoder.encode(registerRequest.password()))
                           .build();

        userRepository.save(newUser);

        return new ApiSuccessResponse<>(true, "User registered successfully", null);
    }

    @Override
    public ApiSuccessResponse<LoginResponse> login(LoginRequest req) {
        var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.identifier(),
                                                                                    req.password()));

        var principal = (UserDetailsImpl) auth.getPrincipal();
        var token = jwt.generate(principal);
        var roles = principal.getAuthorities()
                             .stream()
                             .map(GrantedAuthority::getAuthority)
                             .toList();

        log.info("Login success user={} roles={}", principal.getUsername(), roles);

        LoginResponse response = new LoginResponse(principal.getId().toString(),
                                                   token,
                                                   principal.getFirstName(),
                                                   principal.getLastName(),
                                                   principal.getUsername(),
                                                   principal.getEmail(),
                                                   roles.getFirst());

        return new ApiSuccessResponse<>(true, "Login success", response);
    }
}
