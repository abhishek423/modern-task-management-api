package com.abdev.taskmanager.auth.controller;

import com.abdev.taskmanager.auth.dto.request.*;
import com.abdev.taskmanager.auth.dto.response.AuthResponse;
import com.abdev.taskmanager.auth.entity.RefreshToken;
import com.abdev.taskmanager.auth.service.AuthService;
import com.abdev.taskmanager.auth.service.PasswordResetService;
import com.abdev.taskmanager.auth.service.RefreshTokenService;
import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.security.jwt.JwtService;
import com.abdev.taskmanager.security.principal.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetService passwordResetService;


    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {

        RefreshToken oldToken = refreshTokenService.findByToken(
                        request.getRefreshToken()
                )
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() ->
                        new RuntimeException("Invalid refresh token"));

        User user = oldToken.getUser();

        // ROTATION
        refreshTokenService.deleteByToken(oldToken.getToken());

        RefreshToken newRefreshToken =
                refreshTokenService.createRefreshToken(user);

        UserPrincipal userPrincipal = new UserPrincipal(user);

        String newAccessToken =
                jwtService.generateToken(userPrincipal);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken.getToken()
        );
    }

    @PostMapping("/logout")
    public String logout(@RequestBody LogoutRequest request) {

        refreshTokenService.deleteByToken(
                request.getRefreshToken()
        );

        return "Logged out successfully";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        passwordResetService.createResetToken(
                request.getEmail()
        );

        return "Password reset token generated";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestBody ResetPasswordRequest request) {

        passwordResetService.resetPassword(request);

        return "Password reset successful";
    }
}