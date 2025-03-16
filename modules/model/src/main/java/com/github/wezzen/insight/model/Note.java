package com.github.wezzen.insight.model;

import java.util.*;

public class Note {
    public final UUID id;
    public final String category;
    public final String content;
    public final Date createdAt;
    public final List<String> tags;
    public final Date reminder;

    public Note(final String category, final String content, final List<String> tags, final Date reminder) {
        this.id = UUID.randomUUID();
        this.category = category;
        this.content = content;
        this.createdAt = new Date();
        this.tags = tags != null ? tags : new ArrayList<>();
        this.reminder = reminder;
    }

    @Override
    public String toString() {
        return "[" + category + "] " + content + " Tags: " + tags + " Reminder: " + reminder + " (" + createdAt + ")";
    }
}
