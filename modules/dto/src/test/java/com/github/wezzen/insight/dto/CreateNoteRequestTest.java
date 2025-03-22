package com.github.wezzen.insight.dto;

import com.github.wezzen.insight.dto.request.CreateNoteRequest;
import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class CreateNoteRequestTest {

    @Test
    void creatingTest() {

        EqualsVerifier.forClass(CreateNoteRequest.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }

}