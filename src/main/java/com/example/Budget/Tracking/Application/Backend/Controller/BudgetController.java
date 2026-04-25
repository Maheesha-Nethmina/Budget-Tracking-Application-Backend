package com.example.Budget.Tracking.Application.Backend.Controller;

import com.example.Budget.Tracking.Application.Backend.DTO.BudgetRequest;
import com.example.Budget.Tracking.Application.Backend.DTO.ErrorResponse;
import com.example.Budget.Tracking.Application.Backend.Service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("/add/{username}")
    public ResponseEntity<?> addBudget(@RequestBody BudgetRequest request, @PathVariable String username) {
        try {
            return ResponseEntity.ok(budgetService.createBudget(request, username));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/edit/{id}/{username}")
    public ResponseEntity<?> updateBudget(@PathVariable Long id, @RequestBody BudgetRequest request, @PathVariable String username) {
        try {
            return ResponseEntity.ok(budgetService.updateBudget(id, request, username));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}/{username}")
    public ResponseEntity<?> deleteBudget(@PathVariable Long id, @PathVariable String username) {
        try {
            budgetService.deleteBudget(id, username);
            return ResponseEntity.ok().body("{\"message\": \"Budget deleted successfully.\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/all/{username}/{period}")
    public ResponseEntity<?> getBudgets(@PathVariable String username, @PathVariable String period) {
        try {
            return ResponseEntity.ok(budgetService.getBudgetsForPeriod(username, period));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}