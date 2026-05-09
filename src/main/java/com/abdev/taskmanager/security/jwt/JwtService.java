package com.abdev.taskmanager.security.jwt;

import com.abdev.taskmanager.security.principal.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserPrincipal userPrincipal);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
}