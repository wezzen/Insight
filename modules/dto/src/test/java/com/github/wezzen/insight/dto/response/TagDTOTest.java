package com.github.wezzen.insight.dto.response;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TagDTOTest {

    @Test
    void creatingTest() {
        final TagDTO dto = new TagDTO("TestTag", "RED");
        Assertions.assertEquals("TestTag", dto.tag);
    }

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(TagDTO.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }
}