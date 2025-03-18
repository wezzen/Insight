package com.github.wezzen.insight.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode
public class NoteDTO {

    public final String category;

    public final String content;

    public final Set<String> tags;

    public final String createdAt;

    public final String remind;

    @JsonCreator
    public NoteDTO(@JsonProperty("category") final String category,
                   @JsonProperty("content") final String content,
                   @JsonProperty("tags") final Set<String> tags,
                   @JsonProperty("createdAt") final String createdAt,
                   @JsonProperty("remind") final String remind) {
        this.category = category;
        this.content = content;
        this.tags = tags;
        this.createdAt = createdAt;
        this.remind = remind;
    }
}
