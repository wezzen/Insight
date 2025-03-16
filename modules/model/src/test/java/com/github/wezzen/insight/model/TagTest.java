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

}