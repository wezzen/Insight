package com.github.wezzen.insight.service;

import com.github.wezzen.insight.dto.response.CategoryDTO;
import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.model.Note;
import com.github.wezzen.insight.repository.CategoryRepository;
import com.github.wezzen.insight.service.exception.CategoryNotFoundException;
import com.github.wezzen.insight.service.exception.DeleteNotEmptyCategoryException;
import com.github.wezzen.insight.service.exception.DuplicateCategoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryDTO createCategory(final String name) {
        if (categoryRepository.findById(name).isPresent()) {
            throw new DuplicateCategoryException("Category with name " + name + " already exists");
        }
        return convert(categoryRepository.save(new Category(name)));
    }

    public List<CategoryDTO> getAllCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false).map(this::convert).toList();
    }

    @Transactional
    public void deleteCategory(final String name) {
        final Optional<Category> categoryOptional = categoryRepository.findById(name);
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException("Category with name " + name + " does not exist");
        }
        final Set<Note> notes = categoryOptional.get().getNotes();
        if (notes != null && !notes.isEmpty()) {
            throw new DeleteNotEmptyCategoryException("Category with name " + name + " has " + notes.size() + " notes ");
        }
        categoryRepository.deleteById(name);
    }

    protected CategoryDTO convert(final Category category) {
        return new CategoryDTO(category.getName());
    }
}
