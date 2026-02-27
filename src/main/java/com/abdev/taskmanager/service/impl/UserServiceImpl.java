package com.abdev.taskmanager.service.impl;

import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.exception.DuplicateResourceException;
import com.abdev.taskmanager.exception.ResourceNotFoundException;
import com.abdev.taskmanager.repository.UserRepository;
import com.abdev.taskmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {

        log.info("Attempting to create user with email: {}", user.getEmail());

        if(userRepository.existsByEmail(user.getEmail())) {

            log.warn("Duplicate user creation attempt for email: {}", user.getEmail());

            throw new DuplicateResourceException(
                    "User already exists with email: " + user.getEmail()
            );
        }

        User savedUser = userRepository.save(user);

        log.info("User created successfully with id: {}", savedUser.getId());

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {

        log.debug("Fetching user by id: {}", id);

        return userRepository.findById(id)
                .orElseThrow(()-> {

                    log.warn("User not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "User not found with id: " + id
                    );
                });


    }

    @Override
    public User getUserByEmail(String email) {

        log.debug("Fetching user by email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(()-> {

                    log.warn("User not found with email: {}", email);

                    return new ResourceNotFoundException(
                                "User not found with email: " + email
                        );
                });
    }
}
