package com.github.wezzen.insight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wezzen.insight.dto.request.CreateNoteRequest;
import com.github.wezzen.insight.dto.request.UpdateNoteRequest;
import com.github.wezzen.insight.dto.response.NoteDTO;
import com.github.wezzen.insight.model.Category;
import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.service.NoteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
@ContextConfiguration(classes = ControllerTestConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AutoCloseable closeable;
    @Autowired
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.reset(noteService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllNotesEmptySuccessTest() throws Exception {
        Mockito.when(noteService.getAllNotes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAllNotesSuccessTest() throws Exception {
        final List<NoteDTO> noteDTOS = List.of(
                new NoteDTO(
                        "TestCategory1",
                        "TestContent1",
                        Set.of(
                                "TestTag1",
                                "TestTag2"
                        ),
                        "CreatedAt1",
                        "Remind1"
                ),
                new NoteDTO(
                        "TestCategory2",
                        "TestContent2",
                        Set.of(
                                "TestTag3",
                                "TestTag4"
                        ),
                        "CreatedAt2",
                        "Remind2"
                ),
                new NoteDTO(
                        "TestCategory3",
                        "TestContent3",
                        Set.of(
                                "TestTag1",
                                "TestTag4",
                                "TestTag5"
                        ),
                        "CreatedAt3",
                        "Remind3"
                )
        );

        Mockito.when(noteService.getAllNotes()).thenReturn(noteDTOS);

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(noteDTOS.size()))
                .andExpect(jsonPath("$[0].category").value("TestCategory1"))
                .andExpect(jsonPath("$[0].content").value("TestContent1"))
                .andExpect(jsonPath("$[0].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[0].createdAt").value("CreatedAt1"))
                .andExpect(jsonPath("$[0].remind").value("Remind1"))
                .andExpect(jsonPath("$[1].category").value("TestCategory2"))
                .andExpect(jsonPath("$[1].content").value("TestContent2"))
                .andExpect(jsonPath("$[1].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag3")))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag4")))
                .andExpect(jsonPath("$[1].createdAt").value("CreatedAt2"))
                .andExpect(jsonPath("$[1].remind").value("Remind2"))
                .andExpect(jsonPath("$[2].category").value("TestCategory3"))
                .andExpect(jsonPath("$[2].content").value("TestContent3"))
                .andExpect(jsonPath("$[2].tags").value(hasSize(3)))
                .andExpect(jsonPath("$[2].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[2].tags").value(hasItem("TestTag4")))
                .andExpect(jsonPath("$[2].tags").value(hasItem("TestTag5")))
                .andExpect(jsonPath("$[2].createdAt").value("CreatedAt3"))
                .andExpect(jsonPath("$[2].remind").value("Remind3"))
        ;
        Mockito.verify(noteService, Mockito.times(1)).getAllNotes();
    }

    @Test
    void createNoteSuccessTest() throws Exception {
        final NoteDTO noteDTO = new NoteDTO(
                "TestCategory1",
                "TestContent1",
                Set.of(
                        "TestTag1",
                        "TestTag2"
                ),
                "CreatedAt1",
                Long.toString(new Date().getTime())
        );
        final CreateNoteRequest request = new CreateNoteRequest(
                new Category(noteDTO.category),
                noteDTO.content,
                noteDTO.tags.stream().map(Tag::new).collect(Collectors.toSet()),
                new Date(Long.parseLong(noteDTO.remind))
        );
        Mockito.when(noteService.createNote(Mockito.any(Category.class), Mockito.anyString(), Mockito.anySet(), Mockito.any()))
                .thenReturn(noteDTO);

        mockMvc.perform(post("/notes").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("TestCategory1"))
                .andExpect(jsonPath("$.content").value("TestContent1"))
                .andExpect(jsonPath("$.tags").value(hasSize(2)))
                .andExpect(jsonPath("$.tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$.tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$.createdAt").value("CreatedAt1"))
                .andExpect(jsonPath("$.remind").value(noteDTO.remind));
        Mockito.verify(noteService, Mockito.times(1))
                .createNote(Mockito.any(Category.class), Mockito.anyString(), Mockito.anySet(), Mockito.any());
    }

    @Test
    void updateNoteSuccessTest() throws Exception {
        final SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        final String newContent = "NewTestContent1";
        final NoteDTO noteDTO = new NoteDTO(
                "TestCategory1",
                "TestContent1",
                Set.of(
                        "TestTag1",
                        "TestTag2"
                ),
                new Date().toString(),
                new Date().toString()
        );
        final CreateNoteRequest request = new CreateNoteRequest(
                new Category(noteDTO.category),
                noteDTO.content,
                noteDTO.tags.stream().map(Tag::new).collect(Collectors.toSet()),
                formatter.parse(noteDTO.remind)
        );
        Mockito.when(noteService.createNote(Mockito.any(Category.class), Mockito.anyString(), Mockito.anySet(), Mockito.any()))
                .thenReturn(noteDTO);

        mockMvc.perform(post("/notes").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("TestCategory1"))
                .andExpect(jsonPath("$.content").value("TestContent1"))
                .andExpect(jsonPath("$.tags").value(hasSize(2)))
                .andExpect(jsonPath("$.tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$.tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$.createdAt").value(noteDTO.createdAt))
                .andExpect(jsonPath("$.remind").value(noteDTO.remind));
        Mockito.verify(noteService, Mockito.times(1))
                .createNote(Mockito.any(Category.class), Mockito.anyString(), Mockito.anySet(), Mockito.any());

        final UpdateNoteRequest updateRequest = new UpdateNoteRequest(
                noteDTO.category,
                noteDTO.content,
                newContent,
                formatter.parse(noteDTO.createdAt).getTime(),
                Set.of(
                        "TestTag1",
                        "TestTag2",
                        "TestTag3"
                ),
                formatter.parse(noteDTO.remind).getTime()
        );

        final NoteDTO updatedNoteDTO = new NoteDTO(
                "TestCategory1",
                newContent,
                Set.of(
                        "TestTag1",
                        "TestTag2",
                        "TestTag3"
                ),
                noteDTO.createdAt,
                noteDTO.remind
        );

        Mockito.when(noteService.updateNote(
                Mockito.any(Category.class),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anySet(),
                Mockito.any(),
                Mockito.any())
        ).thenReturn(updatedNoteDTO);

        mockMvc.perform(put("/notes/update").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("TestCategory1"))
                .andExpect(jsonPath("$.content").value("NewTestContent1"))
                .andExpect(jsonPath("$.tags").value(hasSize(3)))
                .andExpect(jsonPath("$.tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$.tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$.tags").value(hasItem("TestTag3")))
                .andExpect(jsonPath("$.createdAt").value(new Date(updateRequest.createdAt).toString()))
                .andExpect(jsonPath("$.remind").value(new Date(updateRequest.reminder).toString()));

    }

    @Test
    void findNotesByAllTagsSuccessTest() throws Exception {
        final List<NoteDTO> noteDTOS = List.of(
                new NoteDTO("TestCategory1", "TestContent1", Set.of("TestTag1", "TestTag2"), "CreatedAt1", "Remind1"),
                new NoteDTO("TestCategory2", "TestContent2", Set.of("TestTag1", "TestTag2"), "CreatedAt2", "Remind2"),
                new NoteDTO("TestCategory3", "TestContent3", Set.of("TestTag1", "TestTag2"), "CreatedAt3", "Remind3"),
                new NoteDTO("TestCategory4", "TestContent4", Set.of("TestTag1", "TestTag2"), "CreatedAt4", "Remind4"),
                new NoteDTO("TestCategory5", "TestContent5", Set.of("TestTag1", "TestTag2"), "CreatedAt5", "Remind5")
        );
        Mockito.when(noteService.findByAllTags(Mockito.anySet())).thenReturn(noteDTOS);
        mockMvc.perform(get("/notes/s/tags/all").param("tags", "TestTag1", "TestTag2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(noteDTOS.size()))
                .andExpect(jsonPath("$[0].category").value("TestCategory1"))
                .andExpect(jsonPath("$[0].content").value("TestContent1"))
                .andExpect(jsonPath("$[0].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[0].createdAt").value("CreatedAt1"))
                .andExpect(jsonPath("$[0].remind").value("Remind1"))
                .andExpect(jsonPath("$[1].category").value("TestCategory2"))
                .andExpect(jsonPath("$[1].content").value("TestContent2"))
                .andExpect(jsonPath("$[1].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[1].createdAt").value("CreatedAt2"))
                .andExpect(jsonPath("$[1].remind").value("Remind2"))
                .andExpect(jsonPath("$[2].category").value("TestCategory3"))
                .andExpect(jsonPath("$[2].content").value("TestContent3"))
                .andExpect(jsonPath("$[2].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[2].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[2].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[2].createdAt").value("CreatedAt3"))
                .andExpect(jsonPath("$[2].remind").value("Remind3"))
                .andExpect(jsonPath("$[3].category").value("TestCategory4"))
                .andExpect(jsonPath("$[3].content").value("TestContent4"))
                .andExpect(jsonPath("$[3].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[3].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[3].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[3].createdAt").value("CreatedAt4"))
                .andExpect(jsonPath("$[3].remind").value("Remind4"))
                .andExpect(jsonPath("$[4].category").value("TestCategory5"))
                .andExpect(jsonPath("$[4].content").value("TestContent5"))
                .andExpect(jsonPath("$[4].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[4].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[4].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[4].createdAt").value("CreatedAt5"))
                .andExpect(jsonPath("$[4].remind").value("Remind5"));
        Mockito.verify(noteService, Mockito.times(1)).findByAllTags(Mockito.anySet());
    }

    @Test
    void findNotesByAnyTagsSuccessTest() throws Exception {
        final List<NoteDTO> noteDTOS = List.of(
                new NoteDTO("TestCategory1", "TestContent1", Set.of("TestTag1", "TestTag2"), "CreatedAt1", "Remind1"),
                new NoteDTO("TestCategory2", "TestContent2", Set.of("TestTag1", "TestTag3"), "CreatedAt2", "Remind2"),
                new NoteDTO("TestCategory3", "TestContent3", Set.of("TestTag1", "TestTag4", "TestTag5"), "CreatedAt3", "Remind3"),
                new NoteDTO("TestCategory4", "TestContent4", Set.of("TestTag1"), "CreatedAt4", "Remind4"),
                new NoteDTO("TestCategory5", "TestContent5", Set.of("TestTag1", "TestTag5"), "CreatedAt5", "Remind5")
        );
        Mockito.when(noteService.findByAnyTag(Mockito.anySet())).thenReturn(noteDTOS);
        mockMvc.perform(get("/notes/s/tags/any").param("tags", "TestTag1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(noteDTOS.size()))
                .andExpect(jsonPath("$[0].category").value("TestCategory1"))
                .andExpect(jsonPath("$[0].content").value("TestContent1"))
                .andExpect(jsonPath("$[0].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[0].createdAt").value("CreatedAt1"))
                .andExpect(jsonPath("$[0].remind").value("Remind1"))
                .andExpect(jsonPath("$[1].category").value("TestCategory2"))
                .andExpect(jsonPath("$[1].content").value("TestContent2"))
                .andExpect(jsonPath("$[1].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[1].createdAt").value("CreatedAt2"))
                .andExpect(jsonPath("$[1].remind").value("Remind2"))
                .andExpect(jsonPath("$[2].category").value("TestCategory3"))
                .andExpect(jsonPath("$[2].content").value("TestContent3"))
                .andExpect(jsonPath("$[2].tags").value(hasSize(3)))
                .andExpect(jsonPath("$[2].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[2].createdAt").value("CreatedAt3"))
                .andExpect(jsonPath("$[2].remind").value("Remind3"))
                .andExpect(jsonPath("$[3].category").value("TestCategory4"))
                .andExpect(jsonPath("$[3].content").value("TestContent4"))
                .andExpect(jsonPath("$[3].tags").value(hasSize(1)))
                .andExpect(jsonPath("$[3].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[3].createdAt").value("CreatedAt4"))
                .andExpect(jsonPath("$[3].remind").value("Remind4"))
                .andExpect(jsonPath("$[4].category").value("TestCategory5"))
                .andExpect(jsonPath("$[4].content").value("TestContent5"))
                .andExpect(jsonPath("$[4].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[4].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[4].createdAt").value("CreatedAt5"))
                .andExpect(jsonPath("$[4].remind").value("Remind5"));
        Mockito.verify(noteService, Mockito.times(1)).findByAnyTag(Mockito.anySet());
    }
}