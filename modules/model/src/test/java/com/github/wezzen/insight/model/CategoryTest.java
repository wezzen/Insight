package com.github.wezzen.insight.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void creatingTest() {
        final Category category = new Category("Category");
        assertNotNull(category);
        assertEquals("Category", category.getName());
        assertEquals(new Category("Category"), category);
    }

}