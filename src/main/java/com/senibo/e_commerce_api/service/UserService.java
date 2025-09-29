package com.senibo.e_commerce_api.service;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.auth.UserDTO;
import org.springframework.security.core.Authentication;

public interface UserService {
    ApiSuccessResponse<UserDTO> getCurrentUser(Authentication authentication);
}
