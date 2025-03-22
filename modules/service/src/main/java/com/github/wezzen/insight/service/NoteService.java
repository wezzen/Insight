package com.github.wezzen.insight.service;

import com.github.wezzen.insight.dto.response.NoteDTO;
import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.model.Note;
import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.repository.CategoryRepository;
import com.github.wezzen.insight.repository.NoteRepository;
import com.github.wezzen.insight.repository.TagRepository;
import com.github.wezzen.insight.service.exception.CategoryNotFoundException;
import com.github.wezzen.insight.service.exception.NoteNotFoundException;
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
        final Optional<Category> categoryOptional = categoryRepository.findById(category.getName());
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException("Category " + category.getName() + " not found");
        }
        final Category synCategory = categoryOptional.get();
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

    public List<NoteDTO> findByCategory(final String category) {
        final List<NoteDTO> dtos = new ArrayList<>();
        for (final Note note : noteRepository.findAllByCategory(category)) {
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

    public List<NoteDTO> findByAllTags(final Set<Tag> tags) {
        if (tags.isEmpty()) {
            return Collections.emptyList();
        }
        final List<Note> notes = noteRepository.findByAllTagNames(
                tags.stream().map(Tag::getTag).collect(Collectors.toSet()),
                tags.size()
        );
        final List<NoteDTO> dtos = new ArrayList<>();
        for (final Note note : notes) {
            dtos.add(new NoteDTO(
                    note.getCategory().getName(),
                    note.getContent(),
                    note.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()),
                    note.getCreatedAt().toString(),
                    note.getReminder().toString()
            ));
        }
        return dtos;
    }

    public List<NoteDTO> findByAnyTag(final Set<Tag> tags) {
        if (tags.isEmpty()) {
            return Collections.emptyList();
        }
        final List<Note> notes = noteRepository.findByAnyTagName(
                tags.stream().map(Tag::getTag).collect(Collectors.toSet())
        );
        final List<NoteDTO> dtos = new ArrayList<>();
        for (final Note note : notes) {
            dtos.add(new NoteDTO(
                    note.getCategory().getName(),
                    note.getContent(),
                    note.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()),
                    note.getCreatedAt().toString(),
                    note.getReminder().toString()
            ));
        }
        return dtos;
    }

    @Transactional
    public NoteDTO updateNote(final Category category, final String content, final String newContent,
                              final Set<Tag> tags, final Date createdAt, final Date reminder) {
        final Optional<Note> noteOptional = noteRepository.findByCategoryAndContentAndCreatedAt(category, content, createdAt);
        if (noteOptional.isEmpty()) {
            throw new NoteNotFoundException("Note not found with given category, content, and createdAt");
        }
        final Set<Tag> synTags = new HashSet<>();
        for (final Tag tag : tags) {
            final Tag synTag = tagRepository.findById(tag.getTag()).orElseGet(() -> tagRepository.save(tag));
            synTags.add(synTag);
        }
        final Note note = noteOptional.get();
        note.setTags(synTags);
        note.setContent(newContent);
        note.setReminder(reminder);

        final Note updated = noteRepository.save(note);
        return new NoteDTO(
                updated.getCategory().getName(),
                updated.getContent(),
                updated.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()),
                updated.getCreatedAt().toString(),
                updated.getReminder().toString()
        );
    }

    @Transactional
    public void deleteNote(final Category category, final String content, final Date createdAt) {
        final Optional<Note> note = noteRepository
                .findByCategoryAndContentAndCreatedAt(category, content, createdAt);
        if (note.isEmpty()) {
            throw new NoteNotFoundException(String.format("Note not found with given category [%s], content [%s], and createdAt [%s]",
                    category.getName(), content, createdAt));
        }
        noteRepository.deleteById(note.get().getId());
    }
}
