package com.github.wezzen.insight.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

import java.util.Set;

@Immutable
@EqualsAndHashCode
public final class UpdateNoteRequest {

    public final String title;
    public final String category;
    public final String content;
    public final String newContent;
    public final Set<String> tags;
    public final long reminder;

    @JsonCreator
    public UpdateNoteRequest(@JsonProperty("title") final String title,
                             @JsonProperty("category") final String category,
                             @JsonProperty("content") final String content,
                             @JsonProperty("newContent") final String newContent,
                             @JsonProperty("tags") final Set<String> tags,
                             @JsonProperty("reminder") final long reminder) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.newContent = newContent;
        this.tags = tags;
        this.reminder = reminder;
    }
}
