package com.github.wezzen.insight.model;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    void creatingTest() {
        final Tag tag = new Tag("Tag");
        assertNotNull(tag);
        assertEquals("Tag", tag.getTag());
        assertEquals(new Tag("Tag"), tag);
    }

    @Test
    void equalsAndHashCodeTest() {
        final Note note1 = new Note(0L, "TestTitle", "TestContet", null, new Date(), Set.of(new Tag("TestTag1")), new Date());
        final Note note2 = new Note(1L, "TestTitle", "TestContet", null, new Date(), Set.of(new Tag("TestTag1")), new Date());
        EqualsVerifier.forClass(Tag.class)
                .withIgnoredAnnotations(Nonnull.class)
                .withPrefabValues(Note.class, note1, note2)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    void setterTest() {
        final Tag tag = new Tag();
        assertNull(tag.getTag());
        assertNull(tag.getNotes());
        tag.setNotes(Set.of());
        assertNotNull(tag.getNotes());
        assertEquals(Set.of(), tag.getNotes());
    }

}