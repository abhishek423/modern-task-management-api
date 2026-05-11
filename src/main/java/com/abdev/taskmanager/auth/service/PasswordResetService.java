package com.abdev.taskmanager.auth.service;

import com.abdev.taskmanager.auth.dto.request.ResetPasswordRequest;

public interface PasswordResetService {

    void createResetToken(String email);

    void resetPassword(ResetPasswordRequest request);
}
