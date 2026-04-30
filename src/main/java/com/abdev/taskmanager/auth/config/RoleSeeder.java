package com.abdev.taskmanager.auth.config;

import com.abdev.taskmanager.auth.entity.Role;
import com.abdev.taskmanager.auth.entity.enums.RoleType;
import com.abdev.taskmanager.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        for (RoleType roleType : RoleType.values()) {
            roleRepository.findByName(roleType)
                    .orElseGet(() -> roleRepository.save(
                            Role.builder().name(roleType).build()
                    ));
        }
    }
}