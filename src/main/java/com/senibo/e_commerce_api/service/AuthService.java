package com.senibo.e_commerce_api.service;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.auth.LoginRequest;
import com.senibo.e_commerce_api.dto.auth.LoginResponse;
import com.senibo.e_commerce_api.dto.auth.RegisterRequest;

public interface AuthService {
    ApiSuccessResponse<Object> registerUser(RegisterRequest register);

    ApiSuccessResponse<LoginResponse> login(LoginRequest login);
}
