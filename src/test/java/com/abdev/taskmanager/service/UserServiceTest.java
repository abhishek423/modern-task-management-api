package com.abdev.taskmanager.service;


import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.exception.DuplicateResourceException;
import com.abdev.taskmanager.exception.ResourceNotFoundException;
import com.abdev.taskmanager.repository.UserRepository;
import com.abdev.taskmanager.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUserSuccessfully() {

        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.createUser(user);

        assertNotNull(saved);
        assertEquals("test@test.com", saved.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenDuplicateUser() {

        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> userService.createUser(user));
    }

    @Test
    void shouldReturnUserWhenFound() {

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1L));
    }
}
