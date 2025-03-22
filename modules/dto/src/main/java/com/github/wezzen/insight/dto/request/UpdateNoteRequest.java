package com.github.wezzen.insight.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

import java.util.Set;

@Immutable
@EqualsAndHashCode
public final class UpdateNoteRequest {

    public final String category;
    public final String content;
    public final String newContent;
    public final long createdAt;
    public final Set<String> tags;
    public final long reminder;

    @JsonCreator
    public UpdateNoteRequest(@JsonProperty("category") final String category,
                             @JsonProperty("content") final String content,
                             @JsonProperty("newContent") final String newContent,
                             @JsonProperty("createdAt") final long createdAt,
                             @JsonProperty("tags") final Set<String> tags,
                             @JsonProperty("reminder") final long reminder) {
        this.category = category;
        this.content = content;
        this.newContent = newContent;
        this.createdAt = createdAt;
        this.tags = tags;
        this.reminder = reminder;
    }
}
