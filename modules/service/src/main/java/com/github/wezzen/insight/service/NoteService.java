package com.github.wezzen.insight.service;

import com.github.wezzen.insight.dto.response.NoteDTO;
import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.model.Note;
import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.repository.CategoryRepository;
import com.github.wezzen.insight.repository.NoteRepository;
import com.github.wezzen.insight.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    public NoteDTO createNote(final Category category, final String content, final Set<Tag> tags, final Date reminder) {
        final Category synCategory = categoryRepository.findById(category.getName()).orElseGet(() -> categoryRepository.save(category));

        final Set<Tag> synTags = new HashSet<>();
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

        final Note saved = noteRepository.save(note);
        return new NoteDTO(
                saved.getCategory().getName(),
                saved.getContent(),
                saved.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()),
                saved.getCreatedAt().toString(),
                saved.getReminder().toString()
        );
    }

    public List<NoteDTO> getAllNotes() {
        final List<NoteDTO> dtos = new ArrayList<>();
        for (final Note note : noteRepository.findAll()) {
            dtos.add(new NoteDTO(
                    note.getCategory().getName(),
                    note.getContent(),
                    note.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()),
                    note.getCreatedAt().toString(),
                    note.getReminder().toString())
            );
        }
        return dtos;
    }

    @Transactional
    public NoteDTO updateNote(final long id, final Category category, final String content, final Set<Tag> tags,
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

        final Note saved = noteRepository.save(note);
        return new NoteDTO(
                saved.getCategory().getName(),
                saved.getContent(),
                saved.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()),
                saved.getCreatedAt().toString(),
                saved.getReminder().toString()
        );
    }

    @Transactional
    public void deleteNote(final long id) {
        noteRepository.deleteById(id);
    }
}
