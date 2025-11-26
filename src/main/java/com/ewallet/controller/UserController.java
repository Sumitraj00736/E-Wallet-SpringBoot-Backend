package com.ewallet.controller;

import com.ewallet.model.User;
import com.ewallet.service.UserService;
import com.ewallet.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (userOpt.isPresent()) {
            String token = jwtUtil.generateToken(userOpt.get().getId());
            return ResponseEntity.ok().body(token);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    // Get profile (using JWT)
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String userId = jwtUtil.getUserIdFromToken(token);

        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

}
