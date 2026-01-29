package com.abdev.taskmanager.service;

import com.abdev.taskmanager.entity.User;

public interface UserService {
    User createUser(User user);

    User getUserById(Long id);

    User getUserByEmail(String email);
}
