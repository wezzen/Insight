package com.github.wezzen.insight.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

@Immutable
@EqualsAndHashCode
public final class TagResponse {

    public final String tag;

    public final String color;

    @JsonCreator
    public TagResponse(@JsonProperty("tag") final String tag,
                       @JsonProperty("color") final String color) {
        this.tag = tag;
        this.color = color;
    }
}
