package com.github.wezzen.insight.dto.request;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateTagRequestTest {

    @Test
    void creatingTest() {
        final CreateTagRequest request = new CreateTagRequest("TestTag");
        assertEquals("TestTag", request.tag);
    }

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(CreateTagRequest.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }

}