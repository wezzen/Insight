package com.github.wezzen.insight.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    private String content;
    @ManyToOne
    @JoinColumn(name = "category_name")
    private Category category;
    private Date createdAt;
    @ManyToMany
    @JoinTable(
            name = "notes_tags",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name")
    )
    private Set<Tag> tags;
    private Date reminder;
}
