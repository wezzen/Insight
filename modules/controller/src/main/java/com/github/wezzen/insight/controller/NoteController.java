package com.github.wezzen.insight.controller;

import com.github.wezzen.insight.dto.request.CreateNoteRequest;
import com.github.wezzen.insight.dto.request.DeleteNoteRequest;
import com.github.wezzen.insight.dto.request.UpdateNoteRequest;
import com.github.wezzen.insight.dto.response.NoteResponse;
import com.github.wezzen.insight.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> addNewNote(@RequestBody final CreateNoteRequest request) {
        final NoteResponse response = noteService.createNote(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getALlNotes() {
        final List<NoteResponse> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    @PutMapping("/update")
    public ResponseEntity<NoteResponse> updateNote(@RequestBody final UpdateNoteRequest request) {
        final NoteResponse updatedNote = noteService.updateNote(request);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteNote(@RequestBody final DeleteNoteRequest request) {
        noteService.deleteNote(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/s/tags/all")
    public ResponseEntity<List<NoteResponse>> searchNotesByAllTags(@RequestParam(name = "tags") final Set<String> tagNames) {
        return ResponseEntity.ok(noteService.findByAllTags(tagNames));
    }

    @GetMapping("/s/category/{categoryName}")
    public ResponseEntity<List<NoteResponse>> searchNotesByCategory(@PathVariable("categoryName") final String categoryName) {
        return ResponseEntity.ok(noteService.findByCategory(categoryName));
    }

}
