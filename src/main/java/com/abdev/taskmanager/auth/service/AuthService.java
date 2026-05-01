package com.abdev.taskmanager.auth.service;

import com.abdev.taskmanager.auth.dto.request.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
}
