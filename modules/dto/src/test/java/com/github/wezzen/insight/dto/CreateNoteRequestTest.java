package com.github.wezzen.insight.dto;

import com.github.wezzen.insight.model.Category;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateNoteRequestTest {

    @Test
    void creatingTest() {
        final Category category = new Category("Category");
        final Date date = Mockito.mock(Date.class);
        final CreateNoteRequest request1 = new CreateNoteRequest(
                category,
                "Content",
                Set.of(),
                date
        );
        final CreateNoteRequest request2 = new CreateNoteRequest(
                category,
                "Content",
                Set.of(),
                date
        );
        final CreateNoteRequest request3 = new CreateNoteRequest(
                category,
                "Other Content",
                Set.of(),
                Mockito.mock(Date.class)
        );
        assertEquals(request1, request1);
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

}