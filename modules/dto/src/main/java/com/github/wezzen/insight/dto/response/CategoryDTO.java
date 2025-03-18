package com.github.wezzen.insight.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class CategoryDTO {

    public final String name;

    @JsonCreator
    public CategoryDTO(@JsonProperty("name") final String name) {
        this.name = name;
    }
}
