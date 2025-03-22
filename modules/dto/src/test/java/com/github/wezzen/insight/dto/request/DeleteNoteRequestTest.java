package com.github.wezzen.insight.dto.request;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DeleteNoteRequestTest {

    @Test
    void creatingTest() {
        final DeleteNoteRequest request = new DeleteNoteRequest(
                "TestCategory",
                "TestContent",
                12345L
        );
        Assertions.assertEquals("TestCategory", request.category);
        Assertions.assertEquals("TestContent", request.content);
        Assertions.assertEquals(12345L, request.createdAt);
    }

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(DeleteNoteRequest.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }

}