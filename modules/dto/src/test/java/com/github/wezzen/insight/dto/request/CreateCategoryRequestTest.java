package com.github.wezzen.insight.dto.request;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateCategoryRequestTest {

    @Test
    void creatingTest() {
        final CreateCategoryRequest request = new CreateCategoryRequest("TestCategory");
        assertEquals("TestCategory", request.name);
    }

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(CreateCategoryRequest.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }
}