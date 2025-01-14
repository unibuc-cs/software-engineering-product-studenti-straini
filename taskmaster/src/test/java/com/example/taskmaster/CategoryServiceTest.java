package com.example.taskmaster;

import com.example.taskmaster.model.Category;
import com.example.taskmaster.repository.CategoryRepository;
import com.example.taskmaster.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testUpdateCategory() {
        Category category = new Category();
        category.setName("Old Name");
        category = categoryRepository.save(category);

        Category updatedCategory = categoryService.updateCategory(category.getId(), "New Name");
        assertEquals("New Name", updatedCategory.getName());
    }

    @Test
    public void testDeleteCategory() {
        Category category = new Category();
        category.setName("Temporary Category");
        category = categoryRepository.save(category);

        categoryService.deleteCategory(category.getId());
        assertFalse(categoryRepository.existsById(category.getId()));
    }
    @Autowired
    private CategoryService categoryService;

    @Test
    public void testCreateCategory() {
        Category category = categoryService.createCategory("Work");
        assertNotNull(category);
        assertEquals("Work", category.getName());
    }
}
