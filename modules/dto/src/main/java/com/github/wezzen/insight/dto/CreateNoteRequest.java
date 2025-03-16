package com.github.wezzen.insight.dto;


import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.model.Tag;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@EqualsAndHashCode
public class CreateNoteRequest {
    public final Category category;
    public final String content;
    public final Set<Tag> tags;
    public final Date reminder;
}
