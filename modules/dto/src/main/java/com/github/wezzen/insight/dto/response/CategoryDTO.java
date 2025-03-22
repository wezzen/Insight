package com.github.wezzen.insight.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

@Immutable
@EqualsAndHashCode
public final class CategoryDTO {

    public final String name;

    @JsonCreator
    public CategoryDTO(@JsonProperty("name") final String name) {
        this.name = name;
    }
}
