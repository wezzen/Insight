package com.github.wezzen.insight.dto.request;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class CreateNoteRequestTest {

    @Test
    void creatingTest() {
        final CreateNoteRequest request = new CreateNoteRequest(
                "TestCategory",
                "TestContent",
                Set.of(
                        "TestTag1",
                        "TestTag2"
                ),
                12345L
        );
        Assertions.assertEquals("TestCategory", request.category);
        Assertions.assertEquals("TestContent", request.content);
        Assertions.assertEquals(Set.of("TestTag1", "TestTag2"), request.tags);
        Assertions.assertEquals(12345L, request.reminder);
    }

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(CreateNoteRequest.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }
}