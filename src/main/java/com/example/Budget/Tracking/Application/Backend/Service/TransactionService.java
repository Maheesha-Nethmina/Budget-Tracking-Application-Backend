package com.example.Budget.Tracking.Application.Backend.Service;

import com.example.Budget.Tracking.Application.Backend.DTO.TransactionRequest;
import com.example.Budget.Tracking.Application.Backend.DTO.TransactionResponse;
import com.example.Budget.Tracking.Application.Backend.Entity.Category;
import com.example.Budget.Tracking.Application.Backend.Entity.Transaction;
import com.example.Budget.Tracking.Application.Backend.Entity.User;
import com.example.Budget.Tracking.Application.Backend.Repository.CategoryRepository;
import com.example.Budget.Tracking.Application.Backend.Repository.TransactionRepository;
import com.example.Budget.Tracking.Application.Backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    //create transaction
    public TransactionResponse createTransaction(TransactionRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Ensure the user actually owns the category they are trying to use
        if (!category.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You cannot use a category that does not belong to you.");
        }

        Transaction transaction = new Transaction();
        transaction.setTitle(request.getTitle());
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setNote(request.getNote());
        transaction.setUser(user);
        transaction.setCategory(category);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToResponse(savedTransaction);
    }
//get all transactions for a user
    public List<TransactionResponse> getAllUserTransactions(String username) {
        return transactionRepository.findByUser_UsernameOrderByTransactionDateDesc(username)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }
//update transaction
    public TransactionResponse updateTransaction(Long id, TransactionRequest request, String username) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to edit this transaction.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        transaction.setTitle(request.getTitle());
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setNote(request.getNote());
        transaction.setCategory(category);

        return mapToResponse(transactionRepository.save(transaction));
    }
//delete transaction
    public void deleteTransaction(Long id, String username) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to delete this transaction.");
        }

        transactionRepository.delete(transaction);
    }

    //  filter Methods
    public List<TransactionResponse> getTransactionsByDateRange(String username, LocalDate start, LocalDate end) {
        return transactionRepository.findByDateRange(username, start, end)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .title(transaction.getTitle())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .transactionDate(transaction.getTransactionDate())
                .note(transaction.getNote())
                .categoryId(transaction.getCategory().getCategoryId())
                .categoryName(transaction.getCategory().getName())
                .build();
    }
}