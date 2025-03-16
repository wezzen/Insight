package com.github.wezzen.insight;

import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category createCategory(final String name) {
        if (categoryRepository.findById(name).isPresent()) {
            throw new IllegalArgumentException("Category with name " + name + " already exists");
        }
        return categoryRepository.save(new Category(name));
    }

    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Transactional
    public void deleteCategory(final String name) {
        categoryRepository.deleteById(name);
    }
}
