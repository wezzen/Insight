package com.github.wezzen.insight.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

@Immutable
@EqualsAndHashCode
public final class CreateTagRequest {

    public final String tag;

    @JsonCreator
    public CreateTagRequest(@JsonProperty("tag") final String tag) {
        this.tag = tag;
    }

}
