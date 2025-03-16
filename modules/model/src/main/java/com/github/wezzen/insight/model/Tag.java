package com.github.wezzen.insight.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "tags")
public class Tag {
    @Id
    private String tag;

    @ManyToMany(mappedBy = "tags")
    private Set<Note> notes;

    public Tag(final String tag) {
        this.tag = tag;
    }
}
