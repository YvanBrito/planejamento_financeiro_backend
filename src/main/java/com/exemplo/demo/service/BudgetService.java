package com.exemplo.demo.service;

import com.exemplo.demo.dto.BudgetDTO;
import com.exemplo.demo.dto.CategoryDTO;
import com.exemplo.demo.dto.SubcategoryDTO;
import com.exemplo.demo.model.Budget;
import com.exemplo.demo.model.Category;
import com.exemplo.demo.model.Subcategory;
import com.exemplo.demo.model.User;
import com.exemplo.demo.repository.BudgetRepository;
import com.exemplo.demo.repository.CategoryRepository;
import com.exemplo.demo.repository.SubcategoryRepository;
import com.exemplo.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    public Budget saveBudget(BigDecimal value, String type, String date, String email, Long category_id, Long subcategory_id) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));

        Category category = categoryRepository.findById(category_id)
                .orElseThrow(() -> new Exception("Categoria não encontrada"));

        Subcategory subcategory = subcategoryRepository.findById(subcategory_id)
                .orElseThrow(() -> new Exception("Subcategoria não encontrada"));

        Budget budget = Budget.builder()
                .value(value)
                .type(Budget.BudgetType.valueOf(type))
                .date(LocalDateTime.parse(date))
                .user(user)
                .category(category)
                .subcategory(subcategory)
                .build();

        return budgetRepository.save(budget);
    }

    public List<BudgetDTO> getAllBudgets(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));
        List<Budget> budgets = budgetRepository.findAllByUserId(user.getId());
        return toBudgetDTOs(budgets);
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    public List<BudgetDTO> toBudgetDTOs(List<Budget> budgets) {
        return budgets.stream()
                .map(this::toBudgetDTO)
                .collect(Collectors.toList());
    }

    private BudgetDTO toBudgetDTO(Budget budget) {
        CategoryDTO categoryDTO = new CategoryDTO(
                budget.getCategory().getId(),
                budget.getCategory().getName(),
                budget.getCategory().getSubcategories()
        );
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(
                budget.getSubcategory().getId(),
                budget.getSubcategory().getName()
        );

        return new BudgetDTO(
                budget.getId(),
                budget.getValue(),
                budget.getType(),
                budget.getDate(),
                budget.getCreatedAt(),
                budget.getUpdatedAt(),
                categoryDTO,
                subcategoryDTO
        );
    }
}
