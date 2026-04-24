package com.example.Budget.Tracking.Application.Backend.Controller;

import com.example.Budget.Tracking.Application.Backend.DTO.CategoryRequest;
import com.example.Budget.Tracking.Application.Backend.DTO.ErrorResponse;
import com.example.Budget.Tracking.Application.Backend.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// (Removed java.security.Principal import as we are passing username in the URL instead)

@RestController
@RequestMapping("/api/categories")
//@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add/{username}")
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest request, @PathVariable String username) {
        try {
            // Passing the username directly from the URL path to the service
            return ResponseEntity.ok(categoryService.createCategory(request, username));
        } catch (RuntimeException e) {
            // Catch errors and return a clean 400 Bad Request response
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/all/{username}")
    public ResponseEntity<?> getUserCategories(@PathVariable String username) {
        try {
            // Retrieve and return the user's category list using the URL username
            return ResponseEntity.ok(categoryService.getUserCategories(username));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{categoryId}/{username}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId, @PathVariable String username) {
        try {
            // Pass both the category ID and the username from the URL
            categoryService.deleteCategory(categoryId, username);

            // success message
            return ResponseEntity.ok().body("{\"message\": \"Category deleted successfully.\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}