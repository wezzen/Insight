package com.github.wezzen.insight.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class TagDTO {

    public final String tag;

    @JsonCreator
    public TagDTO(@JsonProperty("tag") final String tag) {
        this.tag = tag;
    }
}
