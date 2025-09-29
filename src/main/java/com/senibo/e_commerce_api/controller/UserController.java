package com.senibo.e_commerce_api.controller;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.auth.UserDTO;
import com.senibo.e_commerce_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "user-controller", description = "Endpoints for user management")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user's profile",
            description = "Retrieves the profile information for the currently authenticated user. Requires a valid JWT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    // This tells Swagger it's a protected endpoint
    public ResponseEntity<ApiSuccessResponse<UserDTO>> getCurrentUser(Authentication authentication) {
        // Spring Security automatically injects the 'Authentication' object for the current user.
        ApiSuccessResponse<UserDTO> response = userService.getCurrentUser(authentication);
        return ResponseEntity.ok(response);
    }
}
