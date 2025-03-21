package com.github.wezzen.insight.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDTOTest {

    @Test
    void creatingTest() {
        final CategoryDTO dto1 = new CategoryDTO("Name 1");
        final CategoryDTO dto2 = new CategoryDTO("Name 1");
        final CategoryDTO dto3 = new CategoryDTO("Name 3");
        assertEquals(dto1, dto1);
        assertEquals(dto1.hashCode(), dto1.hashCode());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto2, dto1);
        assertEquals(dto2.hashCode(), dto1.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
        assertNotEquals(dto3, dto1);
        assertNotEquals(dto3.hashCode(), dto1.hashCode());
    }

}