package com.github.wezzen.insight.model;

import org.junit.jupiter.api.Test;

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
    void equalsTest() {
        final Tag tag1 = new Tag("Tag1");
        final Tag tag2 = new Tag("Tag1");
        final Tag tag3 = new Tag("Tag2");

        assertEquals(tag1, tag1);
        assertEquals(tag1.hashCode(), tag1.hashCode());
        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertEquals(tag2, tag1);
        assertEquals(tag2.hashCode(), tag1.hashCode());
        assertNotEquals(tag1, tag3);
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
        assertNotEquals(tag3, tag1);
        assertNotEquals(tag3.hashCode(), tag1.hashCode());
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