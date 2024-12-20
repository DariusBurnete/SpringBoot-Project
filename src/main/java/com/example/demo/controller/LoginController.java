package com.example.demo.controller;

import com.example.demo.JwtUtil;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody User user) {
        User validUser = userService.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (validUser != null) {
            String token = JwtUtil.generateToken(validUser.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @Getter
    private static class AuthResponse {
        private final String token;

        public AuthResponse(String token) {
            this.token = token;
        }

    }
}