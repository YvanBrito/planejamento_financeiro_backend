package com.exemplo.demo.controller;

import com.exemplo.demo.dto.BudgetDTO;
import com.exemplo.demo.dto.BudgetRequest;
import com.exemplo.demo.security.CustomUserDetails;
import com.exemplo.demo.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budget")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<?> saveBudget(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody BudgetRequest request) {
        try {
            budgetService.saveBudget(request.getValue(), request.getType(), request.getDate(), userDetails.getEmail(), request.getCategory_id(), request.getSubcategory_id());
            return ResponseEntity.ok("Orçamento registrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBudgetById(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            List<BudgetDTO> budgets = budgetService.getAllBudgets(userDetails.getEmail());
            return ResponseEntity.ok(budgets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudgetById(@PathVariable Long id){
        try {
            budgetService.deleteBudget(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
