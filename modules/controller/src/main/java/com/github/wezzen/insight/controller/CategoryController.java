package com.github.wezzen.insight.controller;

import com.github.wezzen.insight.dto.request.CreateCategoryRequest;
import com.github.wezzen.insight.dto.response.CategoryResponse;
import com.github.wezzen.insight.service.CategoryService;
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
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody final CreateCategoryRequest request) {
        final CategoryResponse created = categoryService.createCategory(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("name") final String name) {
        categoryService.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }
}
