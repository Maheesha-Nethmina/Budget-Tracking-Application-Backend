package com.example.Budget.Tracking.Application.Backend.Controller;

import com.example.Budget.Tracking.Application.Backend.DTO.ErrorResponse;
import com.example.Budget.Tracking.Application.Backend.DTO.TransactionRequest;
import com.example.Budget.Tracking.Application.Backend.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // Create a new transaction for a user
    @PostMapping("/add/{username}")
    public ResponseEntity<?> addTransaction(@RequestBody TransactionRequest request, @PathVariable String username) {
        try {
            return ResponseEntity.ok(transactionService.createTransaction(request, username));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
// Get all transactions for a user
    @GetMapping("/all/{username}")
    public ResponseEntity<?> getAllTransactions(@PathVariable String username) {
        try {
            return ResponseEntity.ok(transactionService.getAllUserTransactions(username));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
// Update an existing transaction
    @PutMapping("/edit/{id}/{username}")
    public ResponseEntity<?> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionRequest request,
            @PathVariable String username) {
        try {
            return ResponseEntity.ok(transactionService.updateTransaction(id, request, username));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
// Delete a transaction
    @DeleteMapping("/delete/{id}/{username}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id, @PathVariable String username) {
        try {
            transactionService.deleteTransaction(id, username);
            return ResponseEntity.ok().body("{\"message\": \"Transaction deleted successfully.\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

// Filter endpoint for Date Range
    @GetMapping("/filter/{username}")
    public ResponseEntity<?> filterByDate(
            @PathVariable String username,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            return ResponseEntity.ok(transactionService.getTransactionsByDateRange(username, startDate, endDate));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}