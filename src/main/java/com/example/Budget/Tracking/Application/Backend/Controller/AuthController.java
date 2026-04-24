package com.example.Budget.Tracking.Application.Backend.Controller;

import com.example.Budget.Tracking.Application.Backend.DTO.*;
import com.example.Budget.Tracking.Application.Backend.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (RuntimeException e) {
            // Return 400 Bad Request with the custom error message
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (RuntimeException e) {
            // Return 400 Bad Request with the custom error message
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}