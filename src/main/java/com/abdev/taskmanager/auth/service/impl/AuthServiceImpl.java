package com.abdev.taskmanager.auth.service.impl;


import com.abdev.taskmanager.auth.dto.request.RegisterRequest;
import com.abdev.taskmanager.auth.entity.Role;
import com.abdev.taskmanager.auth.entity.enums.RoleType;
import com.abdev.taskmanager.auth.repository.RoleRepository;
import com.abdev.taskmanager.auth.service.AuthService;
import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .build();

        user.getRoles().add(userRole);

        userRepository.save(user);
    }
}