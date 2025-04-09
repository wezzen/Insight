package com.github.wezzen.insight.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "tag")
@Table(name = "tags")
public class Tag {
    @Id
    @Setter(AccessLevel.NONE)
    private String tag;

    @ManyToMany(mappedBy = "tags")
    private Set<Note> notes;

    @Column(nullable = false)
    private String color;

    public Tag(final String tag, final String color) {
        this.tag = tag;
        this.color = color;
    }

    public Tag(final String tag) {
        this.tag = tag;
    }
}
