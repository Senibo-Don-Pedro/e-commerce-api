package com.senibo.e_commerce_api.service;

import com.senibo.e_commerce_api.dto.auth.AuthResponse;
import com.senibo.e_commerce_api.dto.auth.SigninRequest;
import com.senibo.e_commerce_api.dto.auth.SignupRequest;

public interface AuthService {
    void register(SignupRequest req);

    AuthResponse login(SigninRequest req);
}
