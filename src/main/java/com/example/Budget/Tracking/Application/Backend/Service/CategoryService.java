package com.example.Budget.Tracking.Application.Backend.Service;

import com.example.Budget.Tracking.Application.Backend.DTO.CategoryRequest;
import com.example.Budget.Tracking.Application.Backend.DTO.CategoryResponse;
import com.example.Budget.Tracking.Application.Backend.Entity.Category;
import com.example.Budget.Tracking.Application.Backend.Repository.CategoryRepository;
import com.example.Budget.Tracking.Application.Backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    //create new categeory for user
    public CategoryResponse createCategory(CategoryRequest request, String username) {
        // check user provided a valid category name
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Category name cannot be empty.");
        }
        // check user provided a valid category type
        if (request.getType() == null) {
            throw new RuntimeException("Category type (INCOME or EXPENSE) is required.");
        }
        // Fetch the user from the database to link them to the new category
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));

        var category = Category.builder()
                .name(request.getName())
                .type(request.getType())
                .user(user)
                .build();

        var savedCategory = categoryRepository.save(category);

        return mapToResponse(savedCategory);
    }
//get all categories for user
    public List<CategoryResponse> getUserCategories(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return categoryRepository.findByUser_Id(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
//delete category for user
    public void deleteCategory(Long categoryId, String username) {
        // Find specific category by id
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found."));

        // Verify that the logged-in user actually owns this category before allowing deletion
        if (!category.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not have permission to delete this category.");
        }
        categoryRepository.delete(category);
    }

    // Helper method to securely convert a Category entity into a response DTO
    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .type(category.getType())
                .build();
    }
}