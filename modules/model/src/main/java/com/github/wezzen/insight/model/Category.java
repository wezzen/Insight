package com.github.wezzen.insight.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "categories")
public class Category {
    @Id
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Note> notes;

    public Category(final String name) {
        this.name = name;
    }
}
