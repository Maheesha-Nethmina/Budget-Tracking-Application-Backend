package com.example.Budget.Tracking.Application.Backend.Repository;

import com.example.Budget.Tracking.Application.Backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Allows us to find a user by their username during login
    Optional<User> findByUsername(String username);
}