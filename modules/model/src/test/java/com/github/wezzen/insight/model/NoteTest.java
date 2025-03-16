package com.github.wezzen.insight.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NoteTest {

    @Test
    void creatingTest() {
        final long now = System.currentTimeMillis();
        final long remindTime = now + 3600_000L;
        final Note note = new Note(
                0,
                "Sample",
                new Category("CategorySample"),
                Date.from(Instant.ofEpochMilli(now)),
                Set.of(new Tag("Tag1"), new Tag("Tag2")),
                Date.from(Instant.ofEpochMilli(remindTime))
        );
        assertNotNull(note);
        assertEquals(0, note.getId());
        assertEquals("Sample", note.getContent());
        assertEquals(new Category("CategorySample"), note.getCategory());
        assertEquals(Date.from(Instant.ofEpochMilli(now)), note.getCreatedAt());
        assertEquals(Set.of(new Tag("Tag1"), new Tag("Tag2")), note.getTags());
        assertEquals(Date.from(Instant.ofEpochMilli(remindTime)), note.getReminder());
    }

}