package com.abdev.taskmanager.auth.service.impl;

import com.abdev.taskmanager.auth.dto.request.ResetPasswordRequest;
import com.abdev.taskmanager.auth.entity.PasswordResetToken;
import com.abdev.taskmanager.auth.repository.PasswordResetTokenRepository;
import com.abdev.taskmanager.auth.service.PasswordResetService;
import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    private final long resetExpiryMs = 900000; // 15 mins

    @Override
    public void createResetToken(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        tokenRepository.deleteByUser(user);

        PasswordResetToken token = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(
                        Instant.now().plusMillis(resetExpiryMs)
                )
                .build();

        tokenRepository.save(token);

        // MOCK EMAIL
        System.out.println(
                "Password reset token: " + token.getToken()
        );
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken token = tokenRepository
                .findByToken(request.getToken())
                .orElseThrow(() ->
                        new RuntimeException("Invalid reset token"));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Reset token expired");
        }

        User user = token.getUser();

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        tokenRepository.delete(token);
    }

}