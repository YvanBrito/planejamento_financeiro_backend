package com.exemplo.demo.dto;

import com.exemplo.demo.model.Budget;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BudgetDTO {
    private Long id;
    private BigDecimal value;
    private Budget.BudgetType type;
    private LocalDateTime date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CategoryDTO category;
    private SubcategoryDTO subcategory;
}
