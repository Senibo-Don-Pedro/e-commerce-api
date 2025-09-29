package com.senibo.e_commerce_api.service.impl;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.auth.UserDTO;
import com.senibo.e_commerce_api.exception.auth.AuthenticationException;
import com.senibo.e_commerce_api.security.services.UserDetailsImpl;
import com.senibo.e_commerce_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public ApiSuccessResponse<UserDTO> getCurrentUser(Authentication authentication) {
        // Spring Security provides the 'Authentication' object, which contains the user's details (the principal).
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl principal)) {
            // This case should ideally not be hit if the endpoint is properly secured.
            throw new AuthenticationException("User is not authenticated.");
        }

        // Extract the role from the authorities.
        String role = principal.getAuthorities().stream()
                               .map(GrantedAuthority::getAuthority)
                               .findFirst()
                               .orElse(null); // Or a default role if you prefer

        // Map the principal's details to our UserDto.
        UserDTO userDto = new UserDTO(
                principal.getId().toString(),
                principal.getFirstName(),
                principal.getLastName(),
                principal.getUsername(),
                principal.getEmail(),
                role
        );

        return new ApiSuccessResponse<>(true, "User profile retrieved successfully", userDto);
    }
}
