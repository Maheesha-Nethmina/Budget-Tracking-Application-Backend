package com.example.Budget.Tracking.Application.Backend.Repository;

import com.example.Budget.Tracking.Application.Backend.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser_Id(Long userId);
}