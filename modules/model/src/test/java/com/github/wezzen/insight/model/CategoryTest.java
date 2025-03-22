package com.github.wezzen.insight.model;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void creatingTest() {
        final Category category = new Category("Category");
        category.setNotes(null);
        assertNotNull(category);
        assertEquals("Category", category.getName());
        assertNull(category.getNotes());
        final Category category1 = new Category("TestCategory", Mockito.anySet());
        assertNotNull(category1);
        assertEquals("TestCategory", category1.getName());
    }

    @Test
    void equalsAndHashCodeTest() {
        final Note note1 = new Note(0L, "TestContet", null, new Date(), Set.of(new Tag("TestTag1")), new Date());
        final Note note2 = new Note(1L, "TestContet", null, new Date(), Set.of(new Tag("TestTag1")), new Date());
        EqualsVerifier.forClass(Category.class)
                .withIgnoredAnnotations(Nonnull.class)
                .withPrefabValues(Note.class, note1, note2)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
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