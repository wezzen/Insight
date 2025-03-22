package com.github.wezzen.insight.dto.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.model.Tag;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

import java.util.Date;
import java.util.Set;

@Immutable
@EqualsAndHashCode
public class CreateNoteRequest {
    public final Category category;
    public final String content;
    public final Set<Tag> tags;
    public final Date reminder;

    @JsonCreator
    public CreateNoteRequest(@JsonProperty("category") final Category category,
                             @JsonProperty("content") final String content,
                             @JsonProperty("tags") final Set<Tag> tags,
                             @JsonProperty("reminder") final Date reminder) {
        this.category = category;
        this.content = content;
        this.tags = tags;
        this.reminder = reminder;
    }
}
