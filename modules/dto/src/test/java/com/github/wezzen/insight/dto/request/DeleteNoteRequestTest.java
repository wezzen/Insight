package com.github.wezzen.insight.dto.request;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DeleteNoteRequestTest {

    @Test
    void creatingTest() {
        final Date createdAt = new Date();
        final DeleteNoteRequest request1 = new DeleteNoteRequest(
                "TestCategory1",
                "TestContent1",
                createdAt.getTime()
        );
        final DeleteNoteRequest request2 = new DeleteNoteRequest(
                "TestCategory1",
                "TestContent1",
                createdAt.getTime()
        );
        final DeleteNoteRequest request3 = new DeleteNoteRequest(
                "TestCategory3",
                "TestContent3",
                createdAt.getTime()
        );
        assertEquals(request1, request1);
        assertEquals(request1.hashCode(), request1.hashCode());
        assertEquals(request2, request1);
        assertEquals(request2.hashCode(), request1.hashCode());
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
        assertNotEquals(request3, request1);
        assertNotEquals(request3.hashCode(), request1.hashCode());
    }

}