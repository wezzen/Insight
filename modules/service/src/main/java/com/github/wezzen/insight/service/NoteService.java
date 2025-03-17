package com.github.wezzen.insight.service;

import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.model.Note;
import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.repository.CategoryRepository;
import com.github.wezzen.insight.repository.NoteRepository;
import com.github.wezzen.insight.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    @Autowired
    public NoteService(final NoteRepository noteRepository,
                       final CategoryRepository categoryRepository,
                       final TagRepository tagRepository) {
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Note createNote(final Category category, final String content, final Set<Tag> tags, final Date reminder) {
        final Category synCategory = categoryRepository.findById(category.getName()).orElseGet(() -> categoryRepository.save(category));

        final Set<Tag> synTags = new HashSet<>(tags);
        for (final Tag tag : tags) {
            final Tag synTag = tagRepository.findById(tag.getTag()).orElseGet(() -> tagRepository.save(tag));
            synTags.add(synTag);
        }

        final Note note = new Note();
        note.setCategory(synCategory);
        note.setContent(content);
        note.setTags(synTags);
        note.setReminder(reminder);
        note.setCreatedAt(new Date());

        return noteRepository.save(note);
    }

    public List<Note> getAllNotes() {
        return (List<Note>) noteRepository.findAll();
    }

    @Transactional
    public Note updateNote(final long id, final Category category, final String content, final Set<Tag> tags,
                           final Date reminder) {
        final Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getCategory().equals(category)) {
            throw new RuntimeException("Category does not match");
        }
        final Set<Tag> synTags = new HashSet<>(tags);
        for (final Tag tag : tags) {
            final Tag synTag = tagRepository.findById(tag.getTag()).orElseGet(() -> tagRepository.save(tag));
            synTags.add(synTag);
        }

        note.setTags(synTags);
        note.setContent(content);
        note.setReminder(reminder);

        return noteRepository.save(note);
    }

    @Transactional
    public void deleteNote(final long id) {
        noteRepository.deleteById(id);
    }
}
