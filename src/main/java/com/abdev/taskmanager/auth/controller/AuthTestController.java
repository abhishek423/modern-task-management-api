package com.abdev.taskmanager.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthTestController {

    @GetMapping("/auth/ping")
    public String ping() {
        return "Auth Public Endpoint Working";
    }
}