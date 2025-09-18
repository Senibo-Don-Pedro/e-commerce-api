package com.senibo.e_commerce_api.controller;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.auth.LoginRequest;
import com.senibo.e_commerce_api.dto.auth.LoginResponse;
import com.senibo.e_commerce_api.dto.auth.RegisterRequest;
import com.senibo.e_commerce_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "auth-controller", description = "Endpoints for user registration and login")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Register a new user account",
            description = "Creates a new user. Returns an error if validation fails or if the username or email is already in use.")
    @ApiResponses({@ApiResponse(responseCode = "201",
            description = "User registered successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiSuccessResponse.class),
                    examples = @ExampleObject(value = """
                                {
                                  "success": true,
                                  "message": "User registered successfully",
                                  "data": null
                                }
                            """)))})
    @PostMapping("/register")
    public ResponseEntity<ApiSuccessResponse<Object>> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest) {
        // Call the service, which does the actual work
        ApiSuccessResponse<Object> response = authService.registerUser(registerRequest);

        // Return the response from the service, wrapped in a ResponseEntity with a 201 CREATED status
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(
            summary = "Login to your account",
            description = "Login to your account with either your email or username, and your password."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        ApiSuccessResponse<LoginResponse> response = authService.login(loginRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
