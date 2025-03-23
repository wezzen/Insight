package com.github.wezzen.insight.dto.response;

import jakarta.annotation.Nonnull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CategoryDTOTest {

    @Test
    void creatingTest() {
        final CategoryDTO dto = new CategoryDTO("TestName");
        Assertions.assertEquals("TestName", dto.name);
    }

    @Test
    @Disabled
    void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(CategoryDTO.class)
                .withIgnoredAnnotations(Nonnull.class)
                .verify();
    }

}