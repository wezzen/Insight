package com.github.wezzen.insight.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagDTOTest {

    @Test
    void creatingTest() {
        final TagDTO dto1 = new TagDTO("Tag 1");
        final TagDTO dto2 = new TagDTO("Tag 1");
        final TagDTO dto3 = new TagDTO("Tag 3");
        assertEquals(dto1, dto1);
        assertEquals(dto1.hashCode(), dto1.hashCode());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

}