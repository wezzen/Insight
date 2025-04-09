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
        final NoteResponse dto = noteService.convert(note);
        assertEquals(note.getTitle(), dto.title);
        assertEquals(note.getCategory().getName(), dto.category);
        assertEquals(note.getContent(), dto.content);
        assertEquals(note.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()), dto.tags);
        assertEquals(note.getCreatedAt().toString(), dto.createdAt);
        assertEquals(note.getReminder().toString(), dto.remind);
    }

    @Test
    void createNoteSuccessTest() {
        final Date now = new Date();
        final CreateNoteRequest request = new CreateNoteRequest("TestTitle", "TestCategory", "TestContent", Set.of("TestTag"), now.getTime());
        final Category category = new Category(request.category);
        final Tag tag = new Tag("TestTag");
        final Note note = new Note(0, request.title, request.content, category, now, Set.of(tag), now);
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.of(tag));

        Mockito.when(noteRepository.findById(note.getId())).thenReturn(Optional.empty());
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);
        final NoteResponse createdNote = noteService.createNote(request);

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
        final Date now = new Date();
        final CreateNoteRequest request = new CreateNoteRequest("TestTitle", "TestCategory", "TestContent", Set.of("TestTag"), now.getTime());
        final Category category = new Category(request.category);
        final Tag tag = new Tag("TestTag");
        final Note note = new Note(0, request.title, request.content, category, now, Set.of(tag), now);
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.empty());
        Mockito.when(tagRepository.save(Mockito.any())).thenReturn(tag);
        Mockito.when(noteRepository.findById(note.getId())).thenReturn(Optional.empty());
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);
        assertThrows(TagNotFoundException.class, () -> noteService.createNote(request));
    }

    @Test
    void createNoteNotExistCategoryFailedTest() {
        final Date now = new Date();
        final CreateNoteRequest request = new CreateNoteRequest("TestTitle", "TestCategory", "TestContent", Set.of("TestTag"), now.getTime());
        final Category category = new Category(request.category);
        final Tag tag = new Tag("TestTag");
        final Note note = new Note(0, request.title, request.content, category, now, Set.of(tag), now);
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.empty());
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.of(tag));
        Mockito.when(noteRepository.findById(note.getId())).thenReturn(Optional.empty());
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);
        assertThrows(CategoryNotFoundException.class, () -> noteService.createNote(request));
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
        final List<NoteResponse> fetchedNotes = noteService.getAllNotes();
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
        Mockito.when(categoryRepository.findById(target.getName())).thenReturn(Optional.of(target));
        Mockito.when(noteRepository.findByCategory(target)).thenReturn(noteDTOS);
        final List<NoteResponse> fetchedNotes = noteService.findByCategory(target.getName());
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

        Mockito.verify(noteRepository, Mockito.times(1)).findByCategory(target);
    }

    @Test
    void updateNoteSuccessTest() {
        final String title = "TestTitle";
        final String oldContent = "Test Old Content";
        final String newContent = "Test New Content";
        final Date reminder = new Date();
        final UpdateNoteRequest request = new UpdateNoteRequest(title, "TestCategory", oldContent, newContent, Set.of("TestTag1", "TestTag2"), reminder.getTime());
        final Category category = new Category(request.category);
        final Set<Tag> tags = Set.of(new Tag("TestTag1"), new Tag("TestTag2"));
        final Date createdAt = new Date();
        final Note note = new Note(0, title, oldContent, category, createdAt, tags, reminder);

        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        Mockito.when(noteRepository.findAllByTitleAndCategoryAndContent(Mockito.eq(title), Mockito.eq(category), Mockito.eq(oldContent)))
                .thenReturn(Optional.of(note));
        Mockito.when(tagRepository.findById(Mockito.anyString()))
                .thenAnswer(invocation -> Optional.of(new Tag(invocation.getArgument(0))));
        Mockito.when(noteRepository.save(Mockito.any(Note.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final NoteResponse updatedNote = noteService.updateNote(request);
        assertNotNull(updatedNote);
        assertEquals(newContent, updatedNote.content);
        assertEquals(tags.size(), updatedNote.tags.size());
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), updatedNote.tags);
        Mockito.verify(noteRepository, Mockito.times(1)).save(Mockito.any(Note.class));
    }

    @Test
    void updateNoteFailedTest() {
        final String title = "TestTitle";
        final String content = "Test Old Content";
        final Date reminder = new Date();
        final UpdateNoteRequest request = new UpdateNoteRequest(title, "TestCategory", content, "NewContent", Set.of("TestTag1"), reminder.getTime());
        final Category category = new Category(request.category);
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        Mockito.when(noteRepository.findAllByTitleAndCategoryAndContent(Mockito.eq(title), Mockito.eq(category), Mockito.eq(content)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoteNotFoundException.class, () -> noteService.updateNote(request));
        Mockito.verify(noteRepository, Mockito.times(0)).save(Mockito.any(Note.class));
    }

    @Test
    void updateNoteTagNotFoundFailedTest() {
        final String title = "TestTitle";
        final String oldContent = "Test Old Content";
        final String newContent = "Test New Content";
        final Date reminder = new Date();
        final UpdateNoteRequest request = new UpdateNoteRequest(title, "TestCategory", oldContent, newContent, Set.of("TestTag1", "TestTag2"), reminder.getTime());
        final Category category = new Category(request.category);
        final Tag tag1 = new Tag("Test Tag1", "RED");
        final Tag tag2 = new Tag("Test Tag2", "BLACK");
        final Tag tag3 = new Tag("Test Tag3", "YELLOW");
        final Set<Tag> oldTags = Set.of(tag1, tag2);
        final Date createdAt = new Date();
        final Note note = new Note(0, "TestTitle", oldContent, category, createdAt, oldTags, reminder);
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        Mockito.when(noteRepository.findAllByTitleAndCategoryAndContent(Mockito.eq(title), Mockito.eq(category), Mockito.eq(oldContent)))
                .thenReturn(Optional.of(note));
        Mockito.when(tagRepository.findById(tag1.getTag())).thenReturn(Optional.of(tag1));
        Mockito.when(tagRepository.findById(tag2.getTag())).thenReturn(Optional.of(tag2));
        Mockito.when(tagRepository.findById(tag3.getTag())).thenReturn(Optional.empty());
        Mockito.when(noteRepository.save(Mockito.any(Note.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertThrows(TagNotFoundException.class, () -> noteService.updateNote(request));
    }

    @Test
    void updateNoteCategoryNotFoundFailedTest() {
        final String title = "TestTitle";
        final String oldContent = "Test Old Content";
        final String newContent = "Test New Content";
        final Date reminder = new Date();
        final UpdateNoteRequest request = new UpdateNoteRequest(title, "TestCategory", oldContent, newContent, Set.of("TestTag1", "TestTag2"), reminder.getTime());
        final Category category = new Category(request.category);
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> noteService.updateNote(request));
        Mockito.verify(noteRepository, Mockito.times(0)).save(Mockito.any(Note.class));
    }

    @Test
    void deleteNoteSuccessTest() {
        final Note mockNote = Mockito.mock(Note.class);
        Mockito.when(mockNote.getId()).thenReturn(0L);
        Mockito.when(noteRepository.findAllByTitleAndCategoryAndContent(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(mockNote));
        final DeleteNoteRequest request = new DeleteNoteRequest("TestTitle", "TestCategory", "TestContent");
        final Category mockCategory = Mockito.mock(Category.class);
        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(mockCategory));
        Mockito.when(mockCategory.getName()).thenReturn("TestCategory");
        Mockito.when(mockNote.getTitle()).thenReturn("TestTitle");
        Mockito.when(mockNote.getCategory()).thenReturn(mockCategory);
        Mockito.when(mockNote.getContent()).thenReturn("TestContent");
        noteService.deleteNote(request);
        Mockito.verify(noteRepository, Mockito.times(1)).deleteById(mockNote.getId());
    }

    @Test
    void deleteNoteCategoryNotFoundFailedTest() {
        final Note mockNote = Mockito.mock(Note.class);
        Mockito.when(mockNote.getId()).thenReturn(0L);
        Mockito.when(noteRepository.findAllByTitleAndCategoryAndContent(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(mockNote));
        final DeleteNoteRequest request = new DeleteNoteRequest("TestTitle", "TestCategory", "TestContent");
        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> noteService.deleteNote(request));
        Mockito.verify(noteRepository, Mockito.times(0)).deleteById(mockNote.getId());
    }

    @Test
    void deleteNoteFailedTest() {
        final String title = "TestTitle";
        final Category category = new Category("Test Category");
        final String content = "Test Content";
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        Mockito.when(noteRepository.findAllByTitleAndCategoryAndContent(title, category, content))
                .thenReturn(Optional.empty());
        final DeleteNoteRequest request = new DeleteNoteRequest(title, category.getName(), content);
        Assertions.assertThrows(NoteNotFoundException.class, () -> noteService.deleteNote(request));
        Mockito.verify(noteRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
    }

    @Test
    void findNotesByAllTagsSuccessTest() {
        final Set<Tag> tags = Set.of(new Tag("TestTag1"), new Tag("TestTag2"));
        final Category mockCategory = Mockito.mock(Category.class);
        final Date date = new Date();
        final List<Note> noteResponse = List.of(
                new Note(0L, "TestTitle1", "Test Content1", mockCategory, date, tags, date),
                new Note(1L, "TestTitle2", "Test Content2", mockCategory, date, tags, date),
                new Note(2L, "TestTitle3", "Test Content3", mockCategory, date, tags, date),
                new Note(3L, "TestTitle4", "Test Content4", mockCategory, date, tags, date)
        );
        final Set<String> request = Set.of("TestTag1", "TestTag2");
        Mockito.when(tagRepository.findById("TestTag1")).thenReturn(Optional.of(new Tag("TestTag1", "RED")));
        Mockito.when(tagRepository.findById("TestTag2")).thenReturn(Optional.of(new Tag("TestTag2", "YELLOW")));
        Mockito.when(noteRepository.findByTagsContainingAll(tags, tags.size())).thenReturn(noteResponse);
        final List<NoteResponse> fetchedNoteResponses = noteService.findByAllTags(request);
        assertNotNull(fetchedNoteResponses);
        assertEquals(noteResponse.size(), fetchedNoteResponses.size());
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteResponses.getFirst().tags);
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteResponses.get(1).tags);
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteResponses.get(2).tags);
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), fetchedNoteResponses.get(3).tags);
        Mockito.verify(noteRepository, Mockito.times(1)).findByTagsContainingAll(Mockito.anySet(), Mockito.anyLong());
    }

    @Test
    void findNotesByAllTagsEmptySetSuccessTest() {
        final List<NoteResponse> fetchedNoteResponses = noteService.findByAllTags(Set.of());
        assertNotNull(fetchedNoteResponses);
        assertTrue(fetchedNoteResponses.isEmpty());
        Mockito.verify(noteRepository, Mockito.times(0)).findByTagsContainingAll(Mockito.anySet(), Mockito.anyLong());
    }
}