package com.github.wezzen.insight.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

@Immutable
@EqualsAndHashCode
public class CreateCategoryRequest {

    public final String name;

    @JsonCreator
    public CreateCategoryRequest(@JsonProperty("name") final String name) {
        this.name = name;
    }
}
