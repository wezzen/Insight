package com.github.wezzen.insight.controller;

import com.github.wezzen.insight.dto.response.CategoryDTO;
import com.github.wezzen.insight.service.CategoryService;
import com.github.wezzen.insight.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody final Category category) {
        final CategoryDTO created = categoryService.createCategory(category.getName());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("name") final String name) {
        categoryService.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }
}
