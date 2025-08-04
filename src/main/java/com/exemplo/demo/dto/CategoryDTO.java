package com.exemplo.demo.dto;

import com.exemplo.demo.model.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private List<Subcategory> subcategories;
}
