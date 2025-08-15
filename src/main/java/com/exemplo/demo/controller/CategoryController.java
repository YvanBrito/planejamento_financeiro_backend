package com.exemplo.demo.controller;


import com.exemplo.demo.dto.BudgetDTO;
import com.exemplo.demo.dto.BudgetRequest;
import com.exemplo.demo.dto.CategoryDTO;
import com.exemplo.demo.dto.CategoryRequest;
import com.exemplo.demo.security.CustomUserDetails;
import com.exemplo.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> saveBudget(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CategoryRequest request) {
        try {
            categoryService.save(request.getName(), userDetails.getEmail());
            return ResponseEntity.ok("Orçamento registrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCategoriesById(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            List<CategoryDTO> categories = categoryService.getAllBudgets(userDetails.getEmail());
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudgetById(@PathVariable Long id){
        try {
            categoryService.deleteBudget(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
