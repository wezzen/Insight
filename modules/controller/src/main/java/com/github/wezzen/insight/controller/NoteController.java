package com.github.wezzen.insight.controller;

import com.github.wezzen.insight.dto.response.NoteDTO;
import com.github.wezzen.insight.service.NoteByTagsMode;
import com.github.wezzen.insight.service.NoteService;
import com.github.wezzen.insight.dto.request.CreateNoteRequest;
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

    // 3. Обновление записи
    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(
            @PathVariable long id,
            @RequestBody CreateNoteRequest request
    ) {
        final NoteDTO updatedNote = noteService.updateNote(
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
    public ResponseEntity<Void> deleteNote(@PathVariable("id") long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteDTO>> searchNotes(@RequestParam(name = "tags") final Set<String> tags,
                                                     @RequestParam(name = "mode", defaultValue = "any") final String mode) {
        final NoteByTagsMode notesByMode = NoteByTagsMode.valueOf(mode.toUpperCase());
        return ResponseEntity.ok(noteService.findByTags(tags, notesByMode));
    }


}
