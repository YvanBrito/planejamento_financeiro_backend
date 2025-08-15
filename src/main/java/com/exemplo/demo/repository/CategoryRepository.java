package com.exemplo.demo.repository;

import com.exemplo.demo.model.Budget;
import com.exemplo.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findAllByUserId(Long userId);
}
