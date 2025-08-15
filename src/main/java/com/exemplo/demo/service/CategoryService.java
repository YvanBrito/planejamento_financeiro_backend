package com.exemplo.demo.service;

import com.exemplo.demo.dto.CategoryDTO;
import com.exemplo.demo.model.Category;
import com.exemplo.demo.model.User;
import com.exemplo.demo.repository.CategoryRepository;
import com.exemplo.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Category save(String name, String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));

        Category category = Category.builder()
                .name(name)
                .user(user)
                .build();

        return categoryRepository.save(category);
    }

    public List<CategoryDTO> getAllBudgets(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));
        List<Category> categories = categoryRepository.findAllByUserId(user.getId());
        return toCategoryDTOs(categories);
    }

    public void deleteBudget(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<CategoryDTO> toCategoryDTOs(List<Category> categories) {
        return categories.stream()
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }

    private CategoryDTO toCategoryDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getSubcategories()
        );
    }
}
