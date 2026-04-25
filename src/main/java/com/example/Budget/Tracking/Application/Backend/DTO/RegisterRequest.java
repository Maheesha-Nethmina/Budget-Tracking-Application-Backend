package com.example.Budget.Tracking.Application.Backend.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
}