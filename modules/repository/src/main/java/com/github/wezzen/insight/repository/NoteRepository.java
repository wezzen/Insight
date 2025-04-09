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

    List<Note> findByCategory(final Category category);

    @Query("SELECT n FROM Note n " +
            "JOIN n.tags t " +
            "WHERE t IN :tags " +
            "GROUP BY n " +
            "HAVING COUNT(DISTINCT t) = :tagCount")
    List<Note> findByTagsContainingAll(@Param("tags") final Set<Tag> tags, @Param("tagCount") final long tagCount);

    /**
     * get a unique Note with category, content and creating date.
     * @return the note.
     */
    Optional<Note> findAllByTitleAndCategoryAndContent(final String title, final Category category, final String content);
}
