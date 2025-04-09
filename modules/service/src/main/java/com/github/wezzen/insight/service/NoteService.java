package com.github.wezzen.insight.service;

import com.github.wezzen.insight.dto.request.CreateNoteRequest;
import com.github.wezzen.insight.dto.request.DeleteNoteRequest;
import com.github.wezzen.insight.dto.request.UpdateNoteRequest;
import com.github.wezzen.insight.dto.response.NoteResponse;
import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.model.Note;
import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.repository.CategoryRepository;
import com.github.wezzen.insight.repository.NoteRepository;
import com.github.wezzen.insight.repository.TagRepository;
import com.github.wezzen.insight.service.exception.CategoryNotFoundException;
import com.github.wezzen.insight.service.exception.NoteNotFoundException;
import com.github.wezzen.insight.service.exception.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public NoteResponse createNote(final CreateNoteRequest request) {
        final Optional<Category> categoryOptional = categoryRepository.findById(request.category);
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException(String.format("Category [%s] not found", request.category));
        }
        final Category fetchedCategory = categoryOptional.get();

        final Set<Tag> fetchedTags = new HashSet<>();
        for (final String tag : request.tags) {
            final Tag synTag = tagRepository.findById(tag).orElseThrow(() -> new TagNotFoundException(String.format("Tag [%s] not found", tag)));
            fetchedTags.add(synTag);
        }
        final Note note = new Note();
        note.setTitle(request.title);
        note.setCategory(fetchedCategory);
        note.setContent(request.content);
        note.setTags(fetchedTags);
        note.setReminder(new Date(request.reminder));
        note.setCreatedAt(new Date());
        return convert(noteRepository.save(note));
    }

    public List<NoteResponse> getAllNotes() {
        return StreamSupport.stream(noteRepository.findAll().spliterator(), false).map(this::convert).toList();
    }

    public List<NoteResponse> findByCategory(final String categoryName) {
        final Category category = categoryRepository.findById(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(String.format("Category [%s] not found", categoryName)));
        return noteRepository.findByCategory(category).stream().map(this::convert).toList();
    }

    public List<NoteResponse> findByAllTags(final Set<String> tagNames) {
        if (tagNames.isEmpty()) {
            return Collections.emptyList();
        }
        final Set<Tag> fetchedTags = new HashSet<>();
        for (final String tag : tagNames) {
            final Tag synTag = tagRepository.findById(tag).orElseThrow(() -> new TagNotFoundException(String.format("Tag [%s] not found", tag)));
            fetchedTags.add(synTag);
        }
        return noteRepository.findByTagsContainingAll(fetchedTags, fetchedTags.size()).stream().map(this::convert).toList();
    }

    @Transactional
    public NoteResponse updateNote(final UpdateNoteRequest request) {
        final Optional<Category> categoryOptional = categoryRepository.findById(request.category);
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException(String.format("Category [%s] not found", request.category));
        }
        final Category fetchedCategory = categoryOptional.get();
        final Optional<Note> noteOptional = noteRepository.findAllByTitleAndCategoryAndContent(request.title, fetchedCategory, request.content);
        if (noteOptional.isEmpty()) {
            throw new NoteNotFoundException(String.format("Note not found with given title [%s], category [%s] and content [%s]",
                    request.title, request.category, request.content));
        }
        final Set<Tag> fetchedTags = new HashSet<>();
        for (final String tag : request.tags) {
            final Tag synTag = tagRepository.findById(tag).orElseThrow(() -> new TagNotFoundException(String.format("Tag [%s] not found", tag)));
            fetchedTags.add(synTag);
        }
        final Note note = noteOptional.get();
        note.setTags(fetchedTags);
        note.setContent(request.newContent);
        note.setReminder(new Date(request.reminder));
        return convert(noteRepository.save(note));
    }

    @Transactional
    public void deleteNote(final DeleteNoteRequest request) {
        final Optional<Category> categoryOptional = categoryRepository.findById(request.category);
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException(String.format("Category [%s] not found", request.category));
        }
        final Category category = categoryOptional.get();
        final Optional<Note> note = noteRepository.findAllByTitleAndCategoryAndContent(request.title, category, request.content);
        if (note.isEmpty()) {
            throw new NoteNotFoundException(String.format("Note not found with given title [%s], category [%s], and content [%s]",
                    request.title, request.category, request.content));
        }
        noteRepository.deleteById(note.get().getId());
    }

    protected NoteResponse convert(final Note note) {
        return new NoteResponse(
                note.getTitle(),
                note.getCategory().getName(),
                note.getContent(),
                note.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()),
                note.getCreatedAt().toString(),
                note.getReminder().toString()
        );
    }
}
