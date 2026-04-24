package com.example.Budget.Tracking.Application.Backend.Service;

import com.example.Budget.Tracking.Application.Backend.DTO.*;
import com.example.Budget.Tracking.Application.Backend.Entity.User;
import com.example.Budget.Tracking.Application.Backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        // Check if username exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user.getUsername());
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse login(LoginRequest request) {
        // Find user or throw error
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect username or password.");
        }

        var jwtToken = jwtService.generateToken(user.getUsername());
        return AuthResponse.builder().token(jwtToken).build();
    }
}