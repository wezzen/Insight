package com.github.wezzen.insight.model;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
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
                0L,
                "Sample",
                new Category("CategorySample"),
                Date.from(Instant.ofEpochMilli(now)),
                Set.of(new Tag("Tag1"), new Tag("Tag2")),
                Date.from(Instant.ofEpochMilli(remindTime))
        );
        assertNotNull(note);
        assertEquals(0L, note.getId());
        assertEquals("Sample", note.getContent());
        assertEquals(new Category("CategorySample"), note.getCategory());
        assertEquals(Date.from(Instant.ofEpochMilli(now)), note.getCreatedAt());
        assertEquals(Set.of(new Tag("Tag1"), new Tag("Tag2")), note.getTags());
        assertEquals(Date.from(Instant.ofEpochMilli(remindTime)), note.getReminder());
    }

    @Test
    void equalsAndHashCodeTest() {
        final Category category1 = new Category("TestCategory1");
        final Category category2 = new Category("TestCategory2");
        final Tag tag1 = new Tag("TestTag1");
        final Tag tag2 = new Tag("TestTag2");
        EqualsVerifier.forClass(Note.class)
                .withIgnoredAnnotations(Nonnull.class)
                .withPrefabValues(Category.class, category1, category2)
                .withPrefabValues(Tag.class, tag1, tag2)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

}