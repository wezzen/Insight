package com.github.wezzen.insight.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

@Immutable
@EqualsAndHashCode
public final class DeleteNoteRequest {

    public final String title;
    public final String category;
    public final String content;

    @JsonCreator
    public DeleteNoteRequest(@JsonProperty("title") final String title,
                             @JsonProperty("category") final String category,
                             @JsonProperty("content") final String content) {
        this.title = title;
        this.category = category;
        this.content = content;
    }
}
