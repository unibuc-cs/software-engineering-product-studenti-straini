package com.example.taskmaster.service;

import com.example.taskmaster.model.Category;
import com.example.taskmaster.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NoSuchElementException("Category not found");
        }
        categoryRepository.deleteById(categoryId);
    }

    public Category updateCategory(Long categoryId, String newName) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
        category.setName(newName);
        return categoryRepository.save(category);
    }

    public Category findOrCreateCategoryByName(String categoryName) {
        return null;
    }


    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

}
