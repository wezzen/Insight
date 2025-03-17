package com.github.wezzen.insight.controller;

import com.github.wezzen.insight.service.NoteService;
import com.github.wezzen.insight.dto.CreateNoteRequest;
import com.github.wezzen.insight.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Note> addNewNote(@RequestBody final CreateNoteRequest request) {
        final Note note = noteService.createNote(
                request.category,
                request.content,
                request.tags,
                request.reminder
        );
        return ResponseEntity.ok(note);
    }

    @GetMapping
    public ResponseEntity<List<Note>> getALlNotes() {
        final List<Note> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    // 3. Обновление записи
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(
            @PathVariable long id,
            @RequestBody CreateNoteRequest request
    ) {
        Note updatedNote = noteService.updateNote(
                id,
                request.category,
                request.content,
                request.tags,
                request.reminder
        );
        return ResponseEntity.ok(updatedNote);
    }

    // 4. Удаление записи
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

}
