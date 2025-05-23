package com.github.wezzen.insight.dto.response;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

class NoteResponseTest {

    @Test
    void creatingTest() {
        final Date createdAt = new Date();
        final Date remind = new Date();
        final NoteResponse dto = new NoteResponse(
                "TestTitle",
                "TestCategory",
                "TestContent",
                Set.of(
                        "TestTag1",
                        "TestTag2"
                ),
                createdAt.toString(),
                remind.toString()
        );
        Assertions.assertEquals("TestTitle", dto.title);
        Assertions.assertEquals("TestCategory", dto.category);
        Assertions.assertEquals("TestContent", dto.content);
        Assertions.assertEquals(Set.of("TestTag1", "TestTag2"), dto.tags);
        Assertions.assertEquals(createdAt.toString(), dto.createdAt);
        Assertions.assertEquals(remind.toString(), dto.remind);
    }

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(NoteResponse.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }
}