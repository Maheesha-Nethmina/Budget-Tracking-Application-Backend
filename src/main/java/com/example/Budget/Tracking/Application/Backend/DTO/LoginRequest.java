package com.example.Budget.Tracking.Application.Backend.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}