package com.github.wezzen.insight.model;

import com.github.wezzen.insight.utils.ColorGenerator;
import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private final ColorGenerator colorGenerator = new ColorGenerator();

    @Test
    void creatingTest() {
        final String color = colorGenerator.generateSoftColor();
        final Tag tag = new Tag("Tag", color);
        assertNotNull(tag);
        assertEquals("Tag", tag.getTag());
        assertEquals(color, tag.getColor());
        assertEquals(new Tag("Tag", color), tag);
    }

    @Test
    void equalsAndHashCodeTest() {
        final String color = colorGenerator.generateSoftColor();
        final Note note1 = new Note(0L, "TestTitle", "TestContet", null, new Date(),
                Set.of(new Tag("TestTag1", color)), new Date());
        final Note note2 = new Note(1L, "TestTitle", "TestContet", null, new Date(),
                Set.of(new Tag("TestTag1", color)), new Date());
        EqualsVerifier.forClass(Tag.class)
                .withIgnoredAnnotations(Nonnull.class)
                .withPrefabValues(Note.class, note1, note2)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    void setterTest() {
        final Tag tag = new Tag();
        final String color = colorGenerator.generateSoftColor();
        assertNull(tag.getTag());
        assertNull(tag.getNotes());
        assertNull(tag.getColor());
        tag.setNotes(Set.of());
        tag.setColor(color);
        assertNotNull(tag.getNotes());
        assertEquals(Set.of(), tag.getNotes());
        assertEquals(color, tag.getColor());
    }

}