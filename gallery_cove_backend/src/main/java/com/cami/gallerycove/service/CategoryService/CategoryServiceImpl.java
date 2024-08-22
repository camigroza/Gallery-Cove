package com.cami.gallerycove.service.CategoryService;

import com.cami.gallerycove.domain.exception.EntityNotFoundException;
import com.cami.gallerycove.domain.model.Category;
import com.cami.gallerycove.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElse(null);
    }

    @Override
    public Category getCategoryByName(String name) {
        Optional<Category> optionalCategory = categoryRepository.findCategoryByName(name);
        return optionalCategory.orElse(null);
    }

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public void updateCategory(Long id, Category updatedCategory) {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);

        if (existingCategoryOptional.isPresent()) {
            Category existingCategory = existingCategoryOptional.get();

            existingCategory.setName(updatedCategory.getName());

            categoryRepository.save(existingCategory);
        } else {
            throw new EntityNotFoundException("Category with ID " + id + " not found");
        }
    }
}
