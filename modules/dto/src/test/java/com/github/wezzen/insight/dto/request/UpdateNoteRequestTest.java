package com.github.wezzen.insight.dto.request;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class UpdateNoteRequestTest {

    @Test
    void creatingTest() {
        final UpdateNoteRequest request = new UpdateNoteRequest(
                "TestTitle",
                "TestCategory",
                "TestContent",
                "TestNewContent",
                Set.of(
                        "TestTag1",
                        "TestTag2"
                ),
                54321L
        );
        Assertions.assertEquals("TestTitle", request.title);
        Assertions.assertEquals("TestCategory", request.category);
        Assertions.assertEquals("TestContent", request.content);
        Assertions.assertEquals("TestNewContent", request.newContent);
        Assertions.assertEquals(Set.of("TestTag1", "TestTag2"), request.tags);
        Assertions.assertEquals(54321L, request.reminder);
    }

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(UpdateNoteRequest.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }

}