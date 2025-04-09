package com.github.wezzen.insight.service;

import com.github.wezzen.insight.dto.request.CreateCategoryRequest;
import com.github.wezzen.insight.dto.response.CategoryResponse;
import com.github.wezzen.insight.model.Note;
import com.github.wezzen.insight.service.exception.CategoryNotFoundException;
import com.github.wezzen.insight.service.exception.DeleteNotEmptyCategoryException;
import com.github.wezzen.insight.service.exception.DuplicateCategoryException;
import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);

    private final CategoryService categoryService = new CategoryService(categoryRepository);

    @Test
    void convertTest() {
        final Category category = new Category("TestCategory");
        final CategoryResponse dto = categoryService.convert(category);
        assertEquals(category.getName(), dto.name);
    }

    @Test
    void createCategorySuccessTest() {
        final CreateCategoryRequest request = new CreateCategoryRequest("TestCategory");
        final Category category = new Category(request.name);
        Mockito.when(categoryRepository.findById(request.name)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);
        final CategoryResponse createdCategory = categoryService.createCategory(request);
        assertNotNull(createdCategory);
        assertEquals(category.getName(), createdCategory.name);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(category);
    }

    @Test
    void createCategoryFailedTest() {
        final CreateCategoryRequest request = new CreateCategoryRequest("TestCategory");
        final Category category = new Category(request.name);
        Mockito.when(categoryRepository.findById(request.name)).thenReturn(Optional.of(category));
        assertThrows(DuplicateCategoryException.class, () -> categoryService.createCategory(request));
        Mockito.verify(categoryRepository, Mockito.times(0)).save(category);
    }

    @Test
    void getCategoriesSuccessTest() {
        final List<Category> categories = List.of(
                new Category("Test Category"),
                new Category("Test Category2"),
                new Category("Test Category3")
        );
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);
        final List<CategoryResponse> fetchedCategories = categoryService.getAllCategories();
        assertNotNull(fetchedCategories);
        assertEquals(categories.size(), fetchedCategories.size());
        assertEquals(categories.get(0).getName(), fetchedCategories.get(0).name);
        assertEquals(categories.get(1).getName(), fetchedCategories.get(1).name);
        assertEquals(categories.get(2).getName(), fetchedCategories.get(2).name);
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deleteCategorySuccessTest() {
        final Category category = new Category("Test Category");
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        categoryService.deleteCategory(category.getName());
        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(category.getName());
    }

    @Test
    void deleteCategoryFailedTest() {
        final Category category = new Category("Test Category");
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(category.getName()));
        Mockito.verify(categoryRepository, Mockito.times(0)).deleteById(category.getName());
    }

    @Test
    void deleteCategoryWithNoteFailedTest() {
        final Note mockNote = Mockito.mock(Note.class);
        final Category category = new Category("Test Category", Set.of(mockNote));
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        assertThrows(DeleteNotEmptyCategoryException.class, () -> categoryService.deleteCategory(category.getName()));
        Mockito.verify(categoryRepository, Mockito.times(0)).deleteById(category.getName());
    }
}