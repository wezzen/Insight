package com.github.wezzen.insight.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void creatingTest() {
        final long now = System.currentTimeMillis();
        final long remindTime = now + 3600_000L;
        final Note note1 = new Note(
                0,
                "Sample",
                new Category("CategorySample"),
                Date.from(Instant.ofEpochMilli(now)),
                Set.of(new Tag("Tag1"), new Tag("Tag2")),
                Date.from(Instant.ofEpochMilli(remindTime))
        );
        assertNotNull(note1);
        assertEquals(0, note1.getId());
        assertEquals("Sample", note1.getContent());
        assertEquals(new Category("CategorySample"), note1.getCategory());
        assertEquals(Date.from(Instant.ofEpochMilli(now)), note1.getCreatedAt());
        assertEquals(Set.of(new Tag("Tag1"), new Tag("Tag2")), note1.getTags());
        assertEquals(Date.from(Instant.ofEpochMilli(remindTime)), note1.getReminder());
        final Note note2 = new Note(
                0,
                "Sample",
                new Category("CategorySample"),
                Date.from(Instant.ofEpochMilli(now)),
                Set.of(new Tag("Tag1"), new Tag("Tag2")),
                Date.from(Instant.ofEpochMilli(remindTime))
        );
        final Note note3 = new Note(
                1,
                "Sample",
                new Category("CategorySample"),
                Date.from(Instant.ofEpochMilli(now)),
                Set.of(new Tag("Tag1"), new Tag("Tag2")),
                Date.from(Instant.ofEpochMilli(remindTime))
        );
        assertEquals(note1, note1);
        assertEquals(note1.hashCode(), note1.hashCode());
        assertEquals(note1, note2);
        assertEquals(note1.hashCode(), note2.hashCode());
        assertEquals(note2, note1);
        assertEquals(note2.hashCode(), note1.hashCode());
        assertNotEquals(note1, note3);
        assertNotEquals(note1.hashCode(), note3.hashCode());
        assertNotEquals(note3, note1);
        assertNotEquals(note3.hashCode(), note1.hashCode());
    }

    @Test
    void equalsTest() {
        final Note note1 = new Note();
        note1.setContent("Note 1");
        final Note note2 = new Note();
        note2.setContent("Note 1");
        final Note note3 = new Note();
        note3.setContent("Note 2");
    }

}