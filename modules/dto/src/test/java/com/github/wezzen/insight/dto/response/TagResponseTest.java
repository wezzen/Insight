package com.github.wezzen.insight.dto.response;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TagResponseTest {

    @Test
    void creatingTest() {
        final TagResponse dto = new TagResponse("TestTag", "RED");
        Assertions.assertEquals("TestTag", dto.tag);
    }

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(TagResponse.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }
}