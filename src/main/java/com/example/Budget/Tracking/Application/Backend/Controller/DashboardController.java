package com.example.Budget.Tracking.Application.Backend.Controller;

import com.example.Budget.Tracking.Application.Backend.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getDashboardSummary(@PathVariable String username) {
        try {
            return ResponseEntity.ok(dashboardService.getDashboardData(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to load dashboard data: " + e.getMessage());
        }
    }
}