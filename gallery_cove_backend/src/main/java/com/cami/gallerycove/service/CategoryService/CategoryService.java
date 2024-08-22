package com.cami.gallerycove.service.CategoryService;

import com.cami.gallerycove.domain.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    void saveCategory(Category category);

    void deleteCategory(Long id);

    void updateCategory(Long id, Category updatedCategory);
}
