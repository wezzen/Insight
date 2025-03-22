package com.github.wezzen.insight.dto.request;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class UpdateNoteRequestTest {

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(UpdateNoteRequest.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }

}