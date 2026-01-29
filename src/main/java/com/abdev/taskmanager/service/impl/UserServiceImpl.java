package com.abdev.taskmanager.service.impl;

import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.exception.DuplicateResourceException;
import com.abdev.taskmanager.exception.ResourceNotFoundException;
import com.abdev.taskmanager.repository.UserRepository;
import com.abdev.taskmanager.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException(
                    "User already exists with email: " + user.getEmail()
            );
        }
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        ));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        ));
    }
}
