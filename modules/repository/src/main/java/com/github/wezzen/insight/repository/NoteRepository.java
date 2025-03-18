package com.github.wezzen.insight.repository;

import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.model.Note;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

    /**
     * get a list of notes that contain ANY of tags from the {@param tagNames}
     * @return list of Notes with tags
     */
    @Query("SELECT n FROM Note n JOIN n.tags t WHERE t.tag IN :tagNames")
    List<Note> findByAnyTagName(@Param("tagNames") final Set<String> tagNames);

    /**
     * get a list of notes that contain each tag from the {@param tagNames}
     * @return list of Notes with tags
     */
    @Query("""
        SELECT n FROM Note n
        JOIN n.tags t
        WHERE t.tag IN :tagNames
        GROUP BY n
        HAVING COUNT(DISTINCT t.tag) = :size
    """)
    List<Note> findByTagNames(@Param("tagNames") final Set<String> tagNames, @Param("size") final long size);

    /**
     * get a list of notes that belongs to the {@param category}
     * @return list of Notes with category
     */
    @Query("SELECT n FROM Note n JOIN n.category c WHERE c.name = :category")
    List<Note> findAllByCategory(@Param("category") final String category);
}
