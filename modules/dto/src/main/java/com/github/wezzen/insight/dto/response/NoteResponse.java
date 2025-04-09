package com.github.wezzen.insight.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

import java.util.Set;

@Immutable
@EqualsAndHashCode
public final class NoteResponse {

    public final String title;

    public final String category;

    public final String content;

    public final Set<String> tags;

    public final String createdAt;

    public final String remind;

    @JsonCreator
    public NoteResponse(@JsonProperty("title") final String title,
                        @JsonProperty("category") final String category,
                        @JsonProperty("content") final String content,
                        @JsonProperty("tags") final Set<String> tags,
                        @JsonProperty("createdAt") final String createdAt,
                        @JsonProperty("remind") final String remind) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.tags = tags;
        this.createdAt = createdAt;
        this.remind = remind;
    }
}
