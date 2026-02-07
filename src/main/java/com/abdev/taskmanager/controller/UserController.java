package com.abdev.taskmanager.controller;

import com.abdev.taskmanager.dto.request.CreateUserRequest;
import com.abdev.taskmanager.dto.response.UserResponse;
import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
           @Valid @RequestBody CreateUserRequest request
    ) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User saved = userService.createUser(user);

        UserResponse response = new UserResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setEmail(saved.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id
    ) {
        User user = userService.getUserById(id);

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
