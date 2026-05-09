package com.abdev.taskmanager.auth.service.impl;

import com.abdev.taskmanager.auth.entity.RefreshToken;
import com.abdev.taskmanager.auth.repository.RefreshTokenRepository;
import com.abdev.taskmanager.auth.service.RefreshTokenService;
import com.abdev.taskmanager.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    private final long refreshTokenDurationMs = 86400000; // 1 day

    @Override
    public RefreshToken createRefreshToken(User user) {

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return repository.save(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }
}
