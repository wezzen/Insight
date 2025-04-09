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
import com.github.wezzen.insight.service.exception.TagNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class NoteServiceTest {

    private final NoteRepository noteRepository = Mockito.mock(NoteRepository.class);

    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);

    private final TagRepository tagRepository = Mockito.mock(TagRepository.class);

    private final NoteService noteService = new NoteService(noteRepository, categoryRepository, tagRepository);

    @Test
    void convertTest() {
        final Date now = new Date();
        final Note note = new Note(0, "TestTitle", "Test Content", new Category("TestCategory"), now,
                Set.of(new Tag("TestTag1"), new Tag("TestTag2")), now);
        final NoteDTO dto = noteService.convert(note);
        assertEquals(note.getTitle(), dto.title);
        assertEquals(note.getCategory().getName(), dto.category);
        assertEquals(note.getContent(), dto.content);
        assertEquals(note.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()), dto.tags);
        assertEquals(note.getCreatedAt().toString(), dto.createdAt);
        assertEquals(note.getReminder().toString(), dto.remind);
    }

    @Test
    void createNoteSuccessTest() {
        final Category category = new Category("Test Category");
        final Tag tag = new Tag("Test Tag");
        final Date now = new Date();
        final Note note = new Note(0, "TestTitle", "Test Content", category, now, Set.of(tag), now);
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.of(tag));

        Mockito.when(noteRepository.findById(note.getId())).thenReturn(Optional.empty());
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);
        final NoteDTO createdNote = noteService.createNote(note.getCategory(), note.getContent(), note.getTags(), note.getReminder());

        assertNotNull(createdNote);
        assertEquals(note.getTitle(), createdNote.title);
        assertEquals(note.getCategory().getName(), createdNote.category);
        assertEquals(note.getContent(), createdNote.content);
        assertEquals(note.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()), createdNote.tags);
        assertEquals(note.getCreatedAt().toString(), createdNote.createdAt);
        assertEquals(note.getReminder().toString(), createdNote.remind);
    }

    @Test
    void createNoteNotExistTagFailedTest() {
        final Category category = new Category("Test Category");
        final Tag tag = new Tag("Test Tag");
        final Date now = new Date();
        final Note note = new Note(0, "TestTitle", "Test Content", category, now, Set.of(tag), now);
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.empty());
        Mockito.when(tagRepository.save(Mockito.any())).thenReturn(tag);

        Mockito.when(noteRepository.findById(note.getId())).thenReturn(Optional.empty());
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);
        assertThrows(TagNotFoundException.class, () -> noteService.createNote(note.getCategory(), note.getContent(), note.getTags(), note.getReminder()));
    }

    @Test
    void createNoteNotExistCategoryFailedTest() {
        final Category category = new Category("Test Category");
        final Tag tag = new Tag("Test Tag");
        final Date now = new Date();
        final Note note = new Note(0, "TestTitle", "Test Content", category, now, Set.of(tag), now);
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.empty());
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.of(tag));

        Mockito.when(noteRepository.findById(note.getId())).thenReturn(Optional.empty());
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);
        assertThrows(CategoryNotFoundException.class,
                () -> noteService.createNote(note.getCategory(), note.getContent(), note.getTags(), note.getReminder()));

        Mockito.verify(noteRepository, Mockito.times(0)).save(Mockito.any(Note.class));
        Mockito.verify(categoryRepository, Mockito.times(0)).save(Mockito.any(Category.class));
        Mockito.verify(tagRepository, Mockito.times(0)).save(Mockito.any(Tag.class));
    }

    @Test
    void getAllNotesSuccessTest() {
        final Tag testTag1 = new Tag("Test tag1");
        final Tag testTag2 = new Tag("Test tag2");
        final Tag testTag3 = new Tag("Test tag3");
        final List<Note> notes = List.of(
                new Note(
                        1,
                        "TestTitle1",
                        "Test Category1",
                        new Category("Test Content1"),
                        new Date(),
                        Set.of(testTag1),
                        new Date()
                ),
                new Note(
                        2,
                        "TestTitle2",
                        "Test Category2",
                        new Category("Test Content2"),
                        new Date(),
                        Set.of(testTag2),
                        new Date()
                ),
                new Note(
                        3,
                        "TestTitle3",
                        "Test Category3",
                        new Category("Test Content3"),
                        new Date(),
                        Set.of(testTag1, testTag3),
                        new Date()
                )
        );
        Mockito.when(noteRepository.findAll()).thenReturn(notes);
        final List<NoteDTO> fetchedNotes = noteService.getAllNotes();
        assertNotNull(fetchedNotes);
        assertEquals(notes.size(), fetchedNotes.size());
        assertEquals(notes.getFirst().getTitle(), fetchedNotes.getFirst().title);
        assertEquals(notes.getFirst().getCategory().getName(), fetchedNotes.getFirst().category);
        assertEquals(notes.getFirst().getContent(), fetchedNotes.getFirst().content);
        assertEquals(notes.getFirst().getTags().stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNotes.getFirst().tags);
        assertEquals(notes.getFirst().getCreatedAt().toString(), fetchedNotes.getFirst().createdAt);
        assertEquals(notes.getFirst().getReminder().toString(), fetchedNotes.getFirst().remind);

        assertEquals(notes.get(1).getTitle(), fetchedNotes.get(1).title);
        assertEquals(notes.get(1).getCategory().getName(), fetchedNotes.get(1).category);
        assertEquals(notes.get(1).getContent(), fetchedNotes.get(1).content);
        assertEquals(notes.get(1).getTags().stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNotes.get(1).tags);
        assertEquals(notes.get(1).getCreatedAt().toString(), fetchedNotes.get(1).createdAt);
        assertEquals(notes.get(1).getReminder().toString(), fetchedNotes.get(1).remind);

        assertEquals(notes.get(2).getTitle(), fetchedNotes.get(2).title);
        assertEquals(notes.get(2).getCategory().getName(), fetchedNotes.get(2).category);
        assertEquals(notes.get(2).getContent(), fetchedNotes.get(2).content);
        assertEquals(notes.get(2).getTags().stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNotes.get(2).tags);
        assertEquals(notes.get(2).getCreatedAt().toString(), fetchedNotes.get(2).createdAt);
        assertEquals(notes.get(2).getReminder().toString(), fetchedNotes.get(2).remind);

        Mockito.verify(noteRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findByCategorySuccessTest() {
        final Tag tag1 = new Tag("TestTag1");
        final Tag tag2 = new Tag("TestTag2");
        final Tag tag3 = new Tag("TestTag3");
        final Category target = new Category("TestCategory1");
        final Date createdAt = new Date();
        final Date remind = new Date();
        final List<Note> noteDTOS = List.of(
                new Note(0L, "TestTitle1", "TestContent1", target, createdAt, Set.of(tag1, tag2), remind),
                new Note(1L, "TestTitle2", "TestContent2", target, createdAt, Set.of(tag1), remind),
                new Note(2L, "TestTitle3", "TestContent3", target, createdAt, Set.of(tag1, tag3), remind)
        );
        Mockito.when(noteRepository.getAllByCategory(target)).thenReturn(noteDTOS);
        final List<NoteDTO> fetchedNotes = noteService.findByCategory(target);
        assertNotNull(fetchedNotes);
        assertEquals(noteDTOS.size(), fetchedNotes.size());
        assertEquals(noteDTOS.getFirst().getTitle(), fetchedNotes.getFirst().title);
        assertEquals(noteDTOS.getFirst().getCategory().getName(), fetchedNotes.getFirst().category);
        assertEquals(noteDTOS.getFirst().getContent(), fetchedNotes.getFirst().content);
        assertEquals(noteDTOS.getFirst().getCreatedAt().toString(), fetchedNotes.getFirst().createdAt);
        assertEquals(noteDTOS.getFirst().getTags().stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNotes.getFirst().tags);
        assertEquals(noteDTOS.getFirst().getReminder().toString(), fetchedNotes.getFirst().remind);

        assertEquals(noteDTOS.get(1).getTitle(), fetchedNotes.get(1).title);
        assertEquals(noteDTOS.get(1).getCategory().getName(), fetchedNotes.get(1).category);
        assertEquals(noteDTOS.get(1).getContent(), fetchedNotes.get(1).content);
        assertEquals(noteDTOS.get(1).getCreatedAt().toString(), fetchedNotes.get(1).createdAt);
        assertEquals(noteDTOS.get(1).getTags().stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNotes.get(1).tags);
        assertEquals(noteDTOS.get(1).getReminder().toString(), fetchedNotes.get(1).remind);

        assertEquals(noteDTOS.get(2).getTitle(), fetchedNotes.get(2).title);
        assertEquals(noteDTOS.get(2).getCategory().getName(), fetchedNotes.get(2).category);
        assertEquals(noteDTOS.get(2).getContent(), fetchedNotes.get(2).content);
        assertEquals(noteDTOS.get(2).getCreatedAt().toString(), fetchedNotes.get(2).createdAt);
        assertEquals(noteDTOS.get(2).getTags().stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNotes.get(2).tags);
        assertEquals(noteDTOS.get(2).getReminder().toString(), fetchedNotes.get(2).remind);

        Mockito.verify(noteRepository, Mockito.times(1)).getAllByCategory(target);
    }

    @Test
    void updateNoteSuccessTest() {
        final String oldContent = "Test Old Content";
        final String newContent = "Test New Content";
        final Category category = new Category("Test Category");
        final Set<Tag> tags = Set.of(new Tag("Test Tag1"), new Tag("Test Tag2"));
        final Date createdAt = new Date();
        final Date reminder = new Date();
        final Note note = new Note(0, "Title", oldContent, category, createdAt, tags, reminder);

        Mockito.when(noteRepository
                        .findByCategoryAndContentAndCreatedAt(Mockito.eq(category), Mockito.eq(oldContent), Mockito.eq(createdAt)))
                .thenReturn(Optional.of(note));

        Mockito.when(tagRepository.findById(Mockito.anyString()))
                .thenAnswer(invocation -> Optional.of(new Tag(invocation.getArgument(0))));
        Mockito.when(noteRepository.save(Mockito.any(Note.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final NoteDTO updatedNote = noteService.updateNote(category, oldContent, newContent, tags, createdAt, reminder);
        assertNotNull(updatedNote);
        assertEquals(newContent, updatedNote.content);
        assertEquals(tags.size(), updatedNote.tags.size());
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), updatedNote.tags);
        Mockito.verify(noteRepository, Mockito.times(1)).save(Mockito.any(Note.class));
    }

    @Test
    void updateNoteFailedTest() {
        final String content = "Test Old Content";
        final Category category = new Category("Test Category");
        final Date createdAt = new Date();

        Mockito.when(noteRepository.findByCategoryAndContentAndCreatedAt(Mockito.eq(category), Mockito.eq(content), Mockito.eq(createdAt)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoteNotFoundException.class,
                () -> noteService.updateNote(category, content, Mockito.anyString(), Mockito.anySet(), createdAt, Mockito.any()));
        Mockito.verify(noteRepository, Mockito.times(0)).save(Mockito.any(Note.class));
    }

    @Test
    void updateNoteTagNotFoundFailedTest() {
        final String oldContent = "Test Old Content";
        final String newContent = "Test New Content";
        final Category category = new Category("Test Category");
        final Tag tag1 = new Tag("Test Tag1", "RED");
        final Tag tag2 = new Tag("Test Tag2", "BLACK");
        final Tag tag3 = new Tag("Test Tag3", "YELLOW");
        final Set<Tag> oldTags = Set.of(tag1, tag2);
        final Set<Tag> newTags = Set.of(tag1, tag2, tag3);
        final Date createdAt = new Date();
        final Date reminder = new Date();
        final Note note = new Note(0, "Title", oldContent, category, createdAt, oldTags, reminder);

        Mockito.when(noteRepository
                        .findByCategoryAndContentAndCreatedAt(Mockito.eq(category), Mockito.eq(oldContent), Mockito.eq(createdAt)))
                .thenReturn(Optional.of(note));

        Mockito.when(tagRepository.findById(tag1.getTag())).thenReturn(Optional.of(tag1));
        Mockito.when(tagRepository.findById(tag2.getTag())).thenReturn(Optional.of(tag2));
        Mockito.when(tagRepository.findById(tag3.getTag())).thenReturn(Optional.empty());
        Mockito.when(noteRepository.save(Mockito.any(Note.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertThrows(TagNotFoundException.class, () -> noteService.updateNote(category, oldContent, newContent, newTags, createdAt, reminder));
    }

    @Test
    void deleteNoteSuccessTest() {
        final Note mockNote = Mockito.mock(Note.class);
        Mockito.when(mockNote.getId()).thenReturn(0L);
        Mockito.when(noteRepository.findByCategoryAndContentAndCreatedAt(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(mockNote));
        noteService.deleteNote(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.verify(noteRepository, Mockito.times(1)).deleteById(mockNote.getId());
    }

    @Test
    void deleteNoteFailedTest() {
        final Category category = new Category("Test Category");
        final String content = "Test Content";
        final Date createdAt = new Date();
        Mockito.when(noteRepository.findByCategoryAndContentAndCreatedAt(category, content, createdAt))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoteNotFoundException.class, () -> noteService.deleteNote(category, content, createdAt));
        Mockito.verify(noteRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
    }

    @Test
    void findNotesByAllTagsSuccessTest() {
        final Set<Tag> tags = Set.of(new Tag("Test Tag1"), new Tag("Test Tag2"));
        final Category mockCategory = Mockito.mock(Category.class);
        final Date date = new Date();
        final List<Note> noteDTOS = List.of(
                new Note(0L, "TestTitle1", "Test Content1", mockCategory, date, tags, date),
                new Note(1L, "TestTitle2", "Test Content2", mockCategory, date, tags, date),
                new Note(2L, "TestTitle3", "Test Content3", mockCategory, date, tags, date),
                new Note(3L, "TestTitle4", "Test Content4", mockCategory, date, tags, date)
        );
        Mockito.when(noteRepository.findAllByTags(tags, tags.size())).thenReturn(noteDTOS);
        final List<NoteDTO> fetchedNoteDTOS = noteService.findByAllTags(tags);
        assertNotNull(fetchedNoteDTOS);
        assertEquals(noteDTOS.size(), fetchedNoteDTOS.size());
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteDTOS.getFirst().tags);
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteDTOS.get(1).tags);
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteDTOS.get(2).tags);
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteDTOS.get(3).tags);
        Mockito.verify(noteRepository, Mockito.times(1)).findAllByTags(Mockito.anySet(), Mockito.anyLong());
    }

    @Test
    void findNotesByAllTagsEmptySetSuccessTest() {
        final List<NoteDTO> fetchedNoteDTOS = noteService.findByAllTags(Set.of());
        assertNotNull(fetchedNoteDTOS);
        assertTrue(fetchedNoteDTOS.isEmpty());
        Mockito.verify(noteRepository, Mockito.times(0)).findAllByTags(Mockito.anySet(), Mockito.anyLong());
    }

    @Test
    void findNotesByAnyTagsSuccessTest() {
        final Tag tag1 = new Tag("Test Tag1");
        final Tag tag2 = new Tag("Test Tag2");
        final Tag tag3 = new Tag("Test Tag3");
        final Set<Tag> tags = Set.of(tag1, tag2, tag3);
        final Category mockCategory = Mockito.mock(Category.class);
        final Date date = new Date();
        final List<Note> noteDTOS = List.of(
                new Note(0L, "TestTitle1", "Test Content1", mockCategory, date, Set.of(tag1, tag2), date),
                new Note(1L, "TestTitle2", "Test Content2", mockCategory, date, Set.of(tag1, tag3), date),
                new Note(2L, "TestTitle3", "Test Content3", mockCategory, date, Set.of(tag1), date),
                new Note(3L, "TestTitle4", "Test Content4", mockCategory, date, Set.of(tag3), date),
                new Note(4L, "TestTitle5", "Test Content5", mockCategory, date, tags, date)
        );

        Mockito.when(noteRepository.getAllByTagsIn(tags)).thenReturn(noteDTOS);
        final List<NoteDTO> fetchedNoteDTOS = noteService.findByAnyTag(tags);
        assertNotNull(fetchedNoteDTOS);
        assertEquals(noteDTOS.size(), fetchedNoteDTOS.size());
        assertEquals(Set.of(tag1, tag2).stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteDTOS.getFirst().tags);
        assertEquals(Set.of(tag1, tag3).stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteDTOS.get(1).tags);
        assertEquals(Set.of(tag1).stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteDTOS.get(2).tags);
        assertEquals(Set.of(tag3).stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteDTOS.get(3).tags);
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteDTOS.get(4).tags);
        Mockito.verify(noteRepository, Mockito.times(1)).getAllByTagsIn(Mockito.anySet());
    }

    @Test
    void findNotesByAnyTagsEmptySetSuccessTest() {
        final List<NoteDTO> fetchedNoteDTOS = noteService.findByAnyTag(Set.of());
        assertNotNull(fetchedNoteDTOS);
        assertTrue(fetchedNoteDTOS.isEmpty());
        Mockito.verify(noteRepository, Mockito.times(0)).getAllByTagsIn(Mockito.anySet());
    }
}