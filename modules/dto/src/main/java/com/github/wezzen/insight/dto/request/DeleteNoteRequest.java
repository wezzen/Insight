package com.github.wezzen.insight.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

@Immutable
@EqualsAndHashCode
public final class DeleteNoteRequest {

    public final String category;
    public final String content;
    public final long createdAt;

    @JsonCreator
    public DeleteNoteRequest(@JsonProperty("category") final String category,
                             @JsonProperty("content") final String content,
                             @JsonProperty("createdAt") final long createdAt) {
        this.category = category;
        this.content = content;
        this.createdAt = createdAt;
    }
}
