package com.github.wezzen.insight.dto.response;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void creatingTest() {
        final ErrorResponse errorResponse = new ErrorResponse("TestCode", "TestMessage");
        assertEquals("TestCode", errorResponse.code);
        assertEquals("TestMessage", errorResponse.message);
    }

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(ErrorResponse.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }

}