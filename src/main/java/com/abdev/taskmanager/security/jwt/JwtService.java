package com.abdev.taskmanager.security.jwt;

import com.abdev.taskmanager.security.principal.UserPrincipal;

public interface JwtService {
    String generateToken(UserPrincipal userPrincipal);
}