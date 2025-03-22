package com.github.wezzen.insight.dto.request;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateNoteRequestTest {

    @Test
    void creatingTest() {
        final Date createdAt = new Date();
        final Date remind = new Date();
        final UpdateNoteRequest request1 = new UpdateNoteRequest(
                "TestCategory1",
                "TestContent1",
                "TestNewContent1",
                createdAt.getTime(),
                Set.of(
                        "TestTag1",
                        "TestTag2"
                ),
                remind.getTime()
        );
        final UpdateNoteRequest request2 = new UpdateNoteRequest(
                "TestCategory1",
                "TestContent1",
                "TestNewContent1",
                createdAt.getTime(),
                Set.of(
                        "TestTag1",
                        "TestTag2"
                ),
                remind.getTime()
        );
        final UpdateNoteRequest request3 = new UpdateNoteRequest(
                "TestCategory3",
                "TestContent1",
                "TestNewContent1",
                createdAt.getTime(),
                Set.of(
                        "TestTag1",
                        "TestTag2"
                ),
                remind.getTime()
        );
        assertEquals(request1, request1);
        assertEquals(request1.hashCode(), request1.hashCode());
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertEquals(request2, request1);
        assertEquals(request2.hashCode(), request1.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
        assertNotEquals(request3, request1);
        assertNotEquals(request3.hashCode(), request1.hashCode());
    }

}