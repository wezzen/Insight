package com.github.wezzen.insight.model;

import org.junit.jupiter.api.Test;

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

        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
    }

}