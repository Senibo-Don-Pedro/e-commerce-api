package com.senibo.e_commerce_api.controller;

import com.senibo.e_commerce_api.dto.ApiResponseDto;
import com.senibo.e_commerce_api.dto.auth.AuthResponse;
import com.senibo.e_commerce_api.dto.auth.SigninRequest;
import com.senibo.e_commerce_api.dto.auth.SignupRequest;
import com.senibo.e_commerce_api.security.services.UserDetailsImpl;
import com.senibo.e_commerce_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<?>> signup(@Valid @RequestBody SignupRequest req) {
        authService.register(req);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(ApiResponseDto.success("User created successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponse>> login(@Valid @RequestBody SigninRequest req) {
        AuthResponse res = authService.login(req);

        return ResponseEntity.ok(ApiResponseDto.success("Login successful", res));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<?>> me(@AuthenticationPrincipal UserDetailsImpl me) {

        if(me == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponseDto.error("bearer token is required")
            );
        }
        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "User profile retrieved successfully",
                        Map.of(
                                "id", me.getId(),
                                "username", me.getUsername(),
                                "email", me.getEmail(),
                                "firstname", me.getFirstName(),
                                "lastname", me.getLastName(),
                                "roles", me.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
                        )
                )
        );
    }


}
