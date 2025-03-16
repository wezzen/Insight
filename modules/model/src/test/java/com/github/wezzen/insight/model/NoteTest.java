package com.github.wezzen.insight.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void creatingTest() {
        final long now = System.currentTimeMillis();
        final Note note = new Note("Sample", "Sample", List.of("tag1", "tag2"), Date.from(Instant.ofEpochMilli(now)));
        assertEquals("Sample", note.category);
        assertEquals("Sample", note.content);
        assertEquals(List.of("tag1", "tag2"), note.tags);
        assertEquals(Date.from(Instant.ofEpochMilli(now)), note.reminder);
    }

}