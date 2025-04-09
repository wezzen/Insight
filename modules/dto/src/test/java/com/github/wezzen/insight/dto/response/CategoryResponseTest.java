package com.github.wezzen.insight.dto.response;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryResponseTest {

    @Test
    void creatingTest() {
        final CategoryResponse dto = new CategoryResponse("TestName");
        Assertions.assertEquals("TestName", dto.name);
    }

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(CategoryResponse.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }

}