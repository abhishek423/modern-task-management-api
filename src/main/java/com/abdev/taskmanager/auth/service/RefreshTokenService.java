package com.abdev.taskmanager.auth.service;

import com.abdev.taskmanager.auth.entity.RefreshToken;
import com.abdev.taskmanager.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    void deleteByToken(String token);
}
