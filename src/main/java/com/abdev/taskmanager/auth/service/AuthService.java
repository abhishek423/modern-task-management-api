package com.abdev.taskmanager.auth.service;

import com.abdev.taskmanager.auth.dto.request.LoginRequest;
import com.abdev.taskmanager.auth.dto.request.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);

    void login(LoginRequest request);
}
