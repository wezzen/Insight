package com.github.wezzen.insight.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

@Immutable
@EqualsAndHashCode
public final class ErrorResponse {
    
    public final String code;
    
    public final String message;

    @JsonCreator
    public ErrorResponse(@JsonProperty("code") final String code,
                         @JsonProperty("message") final String message) {
        this.code = code;
        this.message = message;
    }
}
