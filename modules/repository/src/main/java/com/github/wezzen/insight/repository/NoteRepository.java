package com.github.wezzen.insight.repository;

import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.model.Note;
import com.github.wezzen.insight.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

    /**
     * get a list of notes that contain ANY of tags from the {@param tagNames}
     * @return list of Notes with tags
     */
    List<Note> getAllByTagsIn(final Collection<Tag> tagNames);

    /**
     * get a list of notes that contain each tag from the {@param tags}
     * @return list of Notes with tags
     */
    @Query("""
        SELECT n FROM Note n
        JOIN n.tags t
        WHERE t.tag IN :tags
        GROUP BY n
        HAVING COUNT(DISTINCT t.tag) = :size
    """)
    List<Note> findAllByTags(@Param("tags") final Set<Tag> tags, @Param("size") final long size);

    /**
     * get a list of notes that belongs to the {@param category}
     * @return list of Notes with category
     */
    List<Note> getAllByCategory(final Category category);

    /**
     * get a unique Note with category, content and creating date.
     * @return the note.
     */
    Optional<Note> findByCategoryAndContentAndCreatedAt(final Category category, final String content, final Date createdAt);
}
