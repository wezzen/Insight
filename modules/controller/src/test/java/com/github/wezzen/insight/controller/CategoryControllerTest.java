package com.github.wezzen.insight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wezzen.insight.dto.request.CreateCategoryRequest;
import com.github.wezzen.insight.dto.response.CategoryResponse;
import com.github.wezzen.insight.service.CategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@ContextConfiguration(classes = ControllerTestConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryService categoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.reset(categoryService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllCategoriesTest() throws Exception {
        Mockito.when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createCategoryTest() throws Exception {
        final CategoryResponse category = new CategoryResponse("Test Category");
        Mockito.when(categoryService.createCategory(Mockito.any())).thenReturn(category);
        final CreateCategoryRequest request = new CreateCategoryRequest("Test Category");

        mockMvc.perform(post("/categories").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(category.name));

        Mockito.verify(categoryService, Mockito.times(1)).createCategory(request);
    }

    @Test
    void getCategoriesTest() throws Exception {
        final List<CategoryResponse> categoryResponses = List.of(
                new CategoryResponse("Test Category 1"),
                new CategoryResponse("Test Category 2"),
                new CategoryResponse("Test Category 3")
        );

        Mockito.when(categoryService.getAllCategories()).thenReturn(categoryResponses);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(categoryResponses.size()))
                .andExpect(jsonPath("$[0].name").value("Test Category 1"))
                .andExpect(jsonPath("$[1].name").value("Test Category 2"))
                .andExpect(jsonPath("$[2].name").value("Test Category 3"));

        Mockito.verify(categoryService, Mockito.times(1)).getAllCategories();
    }

    @Test
    void deleteCategoryTest() throws Exception {
        final CategoryResponse category = new CategoryResponse("Test Category");
        Mockito.when(categoryService.createCategory(Mockito.any())).thenReturn(category);
        final CreateCategoryRequest request = new CreateCategoryRequest("Test Category");

        mockMvc.perform(post("/categories").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(category.name));

        Mockito.verify(categoryService, Mockito.times(1)).createCategory(request);

        mockMvc.perform(delete("/categories/{category}", category.name))
                .andExpect(status().isNoContent());

        Mockito.verify(categoryService, Mockito.times(1)).deleteCategory(category.name);
    }
}