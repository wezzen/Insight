package com.github.wezzen.insight.dto.response;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NoteDTOTest {

    @Test
    void creatingTest() {
        final NoteDTO dto1 = new NoteDTO(
                "Category 1",
                "Content 1",
                Set.of("Tag1", "Tag2"),
                "CreateAt 1",
                "Remind 1"
        );
        final NoteDTO dto2 = new NoteDTO(
                "Category 1",
                "Content 1",
                Set.of("Tag1", "Tag2"),
                "CreateAt 1",
                "Remind 1"
        );
        final NoteDTO dto3 = new NoteDTO(
                "Category 2",
                "Content 2",
                Set.of("Tag1", "Tag2"),
                "CreateAt 2",
                "Remind 2"
        );
        assertEquals(dto1, dto1);
        assertEquals(dto1.hashCode(), dto1.hashCode());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

}