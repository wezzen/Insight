package com.github.wezzen.insight.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void creatingTest() {
        final Category category = new Category("Category");
        assertNotNull(category);
        assertEquals("Category", category.getName());
        assertEquals(new Category("Category"), category);
    }

    @Test
    void equalsTest() {
        final Category category1 = new Category("Work");
        final Category category2 = new Category("Work");
        final Category category3 = new Category("Home");

        assertEquals(category1, category1);
        assertEquals(category1.hashCode(), category1.hashCode());
        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());
        assertEquals(category2, category1);
        assertEquals(category2.hashCode(), category1.hashCode());
        assertNotEquals(category1, category3);
        assertNotEquals(category1.hashCode(), category3.hashCode());
        assertNotEquals(category3, category1);
        assertNotEquals(category3.hashCode(), category1.hashCode());
    }

    @Test
    void setterTest() {
        final Category category = new Category();
        assertNull(category.getNotes());
        category.setNotes(Set.of());
        assertNotNull(category.getNotes());
        assertEquals(Set.of(), category.getNotes());
    }

}