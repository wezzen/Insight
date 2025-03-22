package com.github.wezzen.insight.controller;

import com.github.wezzen.insight.dto.request.CreateNoteRequest;
import com.github.wezzen.insight.dto.request.DeleteNoteRequest;
import com.github.wezzen.insight.dto.request.UpdateNoteRequest;
import com.github.wezzen.insight.dto.response.NoteDTO;
import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteDTO> addNewNote(@RequestBody final CreateNoteRequest request) {
        final NoteDTO dto = noteService.createNote(
                request.category,
                request.content,
                request.tags,
                request.reminder
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<NoteDTO>> getALlNotes() {
        final List<NoteDTO> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    @PutMapping("/update")
    public ResponseEntity<NoteDTO> updateNote(@RequestBody final UpdateNoteRequest request) {
        final NoteDTO updatedNote = noteService.updateNote(
                new Category(request.category),
                request.content,
                request.newContent,
                request.tags.stream().map(Tag::new).collect(Collectors.toSet()),
                new Date(request.createdAt),
                new Date(request.reminder)
        );
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteNote(@RequestBody final DeleteNoteRequest request) {
        noteService.deleteNote(
                new Category(request.category),
                request.content,
                new Date(request.createdAt)
        );
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/s/tags/all")
    public ResponseEntity<List<NoteDTO>> searchNotesByAllTags(@RequestParam(name = "tags") final Set<String> tagNames) {
        final Set<Tag> tags = tagNames.stream().map(Tag::new).collect(Collectors.toSet());
        return ResponseEntity.ok(noteService.findByAllTags(tags));
    }

    @GetMapping("/s/tags/any")
    public ResponseEntity<List<NoteDTO>> searchNotesByAnyTags(@RequestParam(name = "tags") final Set<String> tagNames) {
        final Set<Tag> tags = tagNames.stream().map(Tag::new).collect(Collectors.toSet());
        return ResponseEntity.ok(noteService.findByAnyTag(tags));
    }

    @GetMapping("/s/category/{categoryName}")
    public ResponseEntity<List<NoteDTO>> searchNotesByCategory(@PathVariable("categoryName") final String categoryName) {
        return ResponseEntity.ok(noteService.findByCategory(categoryName));
    }

}
