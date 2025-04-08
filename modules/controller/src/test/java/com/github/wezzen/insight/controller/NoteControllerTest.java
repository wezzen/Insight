package com.github.wezzen.insight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wezzen.insight.dto.request.CreateNoteRequest;
import com.github.wezzen.insight.dto.request.DeleteNoteRequest;
import com.github.wezzen.insight.dto.request.UpdateNoteRequest;
import com.github.wezzen.insight.dto.response.NoteDTO;
import com.github.wezzen.insight.model.Category;
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

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
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
                        "TestTitle1",
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
                        "TestTitle2",
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
                        "TestTitle3",
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
                .andExpect(jsonPath("$[0].title").value("TestTitle1"))
                .andExpect(jsonPath("$[0].category").value("TestCategory1"))
                .andExpect(jsonPath("$[0].content").value("TestContent1"))
                .andExpect(jsonPath("$[0].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[0].createdAt").value("CreatedAt1"))
                .andExpect(jsonPath("$[0].remind").value("Remind1"))
                .andExpect(jsonPath("$[1].title").value("TestTitle2"))
                .andExpect(jsonPath("$[1].category").value("TestCategory2"))
                .andExpect(jsonPath("$[1].content").value("TestContent2"))
                .andExpect(jsonPath("$[1].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag3")))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag4")))
                .andExpect(jsonPath("$[1].createdAt").value("CreatedAt2"))
                .andExpect(jsonPath("$[1].remind").value("Remind2"))
                .andExpect(jsonPath("$[2].title").value("TestTitle3"))
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
        final Date createdAt = new Date();
        final Date remind = new Date();
        final NoteDTO noteDTO = new NoteDTO(
                "TestTitle1",
                "TestCategory1",
                "TestContent1",
                Set.of(
                        "TestTag1",
                        "TestTag2"
                ),
                createdAt.toString(),
                remind.toString()
        );
        final CreateNoteRequest request = new CreateNoteRequest(
                noteDTO.category,
                noteDTO.content,
                noteDTO.tags,
                remind.getTime()
        );
        Mockito.when(noteService.createNote(Mockito.any(Category.class), Mockito.anyString(), Mockito.anySet(), Mockito.any()))
                .thenReturn(noteDTO);

        mockMvc.perform(post("/notes").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TestTitle1"))
                .andExpect(jsonPath("$.category").value("TestCategory1"))
                .andExpect(jsonPath("$.content").value("TestContent1"))
                .andExpect(jsonPath("$.tags").value(hasSize(2)))
                .andExpect(jsonPath("$.tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$.tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$.createdAt").value(createdAt.toString()))
                .andExpect(jsonPath("$.remind").value(noteDTO.remind));
        Mockito.verify(noteService, Mockito.times(1))
                .createNote(Mockito.any(Category.class), Mockito.anyString(), Mockito.anySet(), Mockito.any());
    }

    @Test
    void updateNoteSuccessTest() throws Exception {
        final String newContent = "NewTestContent1";
        final Date createdAt = new Date();
        final Date remind = new Date();
        final NoteDTO noteDTO = new NoteDTO(
                "TestTitle1",
                "TestCategory1",
                "TestContent1",
                Set.of(
                        "TestTag1",
                        "TestTag2"
                ),
                createdAt.toString(),
                remind.toString()
        );
        final CreateNoteRequest request = new CreateNoteRequest(
                noteDTO.category,
                noteDTO.content,
                noteDTO.tags,
                remind.getTime()
        );
        Mockito.when(noteService.createNote(Mockito.any(Category.class), Mockito.anyString(), Mockito.anySet(), Mockito.any()))
                .thenReturn(noteDTO);

        mockMvc.perform(post("/notes").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TestTitle1"))
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
                createdAt.getTime(),
                Set.of(
                        "TestTag1",
                        "TestTag2",
                        "TestTag3"
                ),
                remind.getTime()
        );

        final NoteDTO updatedNoteDTO = new NoteDTO(
                "TestTitle1",
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
                .andExpect(jsonPath("$.title").value("TestTitle1"))
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
                new NoteDTO("TestTitle1", "TestCategory1", "TestContent1", Set.of("TestTag1", "TestTag2"), "CreatedAt1", "Remind1"),
                new NoteDTO("TestTitle2","TestCategory2", "TestContent2", Set.of("TestTag1", "TestTag2"), "CreatedAt2", "Remind2"),
                new NoteDTO("TestTitle3","TestCategory3", "TestContent3", Set.of("TestTag1", "TestTag2"), "CreatedAt3", "Remind3"),
                new NoteDTO("TestTitle4","TestCategory4", "TestContent4", Set.of("TestTag1", "TestTag2"), "CreatedAt4", "Remind4"),
                new NoteDTO("TestTitle5","TestCategory5", "TestContent5", Set.of("TestTag1", "TestTag2"), "CreatedAt5", "Remind5")
        );
        Mockito.when(noteService.findByAllTags(Mockito.anySet())).thenReturn(noteDTOS);
        mockMvc.perform(get("/notes/s/tags/all").param("tags", "TestTag1", "TestTag2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(noteDTOS.size()))
                .andExpect(jsonPath("$[0].title").value("TestTitle1"))
                .andExpect(jsonPath("$[0].category").value("TestCategory1"))
                .andExpect(jsonPath("$[0].content").value("TestContent1"))
                .andExpect(jsonPath("$[0].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[0].createdAt").value("CreatedAt1"))
                .andExpect(jsonPath("$[0].remind").value("Remind1"))
                .andExpect(jsonPath("$[1].title").value("TestTitle2"))
                .andExpect(jsonPath("$[1].category").value("TestCategory2"))
                .andExpect(jsonPath("$[1].content").value("TestContent2"))
                .andExpect(jsonPath("$[1].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[1].createdAt").value("CreatedAt2"))
                .andExpect(jsonPath("$[1].remind").value("Remind2"))
                .andExpect(jsonPath("$[2].title").value("TestTitle3"))
                .andExpect(jsonPath("$[2].category").value("TestCategory3"))
                .andExpect(jsonPath("$[2].content").value("TestContent3"))
                .andExpect(jsonPath("$[2].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[2].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[2].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[2].createdAt").value("CreatedAt3"))
                .andExpect(jsonPath("$[2].remind").value("Remind3"))
                .andExpect(jsonPath("$[3].title").value("TestTitle4"))
                .andExpect(jsonPath("$[3].category").value("TestCategory4"))
                .andExpect(jsonPath("$[3].content").value("TestContent4"))
                .andExpect(jsonPath("$[3].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[3].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[3].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[3].createdAt").value("CreatedAt4"))
                .andExpect(jsonPath("$[3].remind").value("Remind4"))
                .andExpect(jsonPath("$[4].title").value("TestTitle5"))
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
                new NoteDTO("TestTitle1", "TestCategory1", "TestContent1", Set.of("TestTag1", "TestTag2"), "CreatedAt1", "Remind1"),
                new NoteDTO("TestTitle2", "TestCategory2", "TestContent2", Set.of("TestTag1", "TestTag3"), "CreatedAt2", "Remind2"),
                new NoteDTO("TestTitle3", "TestCategory3", "TestContent3", Set.of("TestTag1", "TestTag4", "TestTag5"), "CreatedAt3", "Remind3"),
                new NoteDTO("TestTitle4", "TestCategory4", "TestContent4", Set.of("TestTag1"), "CreatedAt4", "Remind4"),
                new NoteDTO("TestTitle5", "TestCategory5", "TestContent5", Set.of("TestTag1", "TestTag5"), "CreatedAt5", "Remind5")
        );
        Mockito.when(noteService.findByAnyTag(Mockito.anySet())).thenReturn(noteDTOS);
        mockMvc.perform(get("/notes/s/tags/any").param("tags", "TestTag1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(noteDTOS.size()))
                .andExpect(jsonPath("$[0].title").value("TestTitle1"))
                .andExpect(jsonPath("$[0].category").value("TestCategory1"))
                .andExpect(jsonPath("$[0].content").value("TestContent1"))
                .andExpect(jsonPath("$[0].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[0].createdAt").value("CreatedAt1"))
                .andExpect(jsonPath("$[0].remind").value("Remind1"))
                .andExpect(jsonPath("$[1].title").value("TestTitle2"))
                .andExpect(jsonPath("$[1].category").value("TestCategory2"))
                .andExpect(jsonPath("$[1].content").value("TestContent2"))
                .andExpect(jsonPath("$[1].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[1].createdAt").value("CreatedAt2"))
                .andExpect(jsonPath("$[1].remind").value("Remind2"))
                .andExpect(jsonPath("$[2].title").value("TestTitle3"))
                .andExpect(jsonPath("$[2].category").value("TestCategory3"))
                .andExpect(jsonPath("$[2].content").value("TestContent3"))
                .andExpect(jsonPath("$[2].tags").value(hasSize(3)))
                .andExpect(jsonPath("$[2].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[2].createdAt").value("CreatedAt3"))
                .andExpect(jsonPath("$[2].remind").value("Remind3"))
                .andExpect(jsonPath("$[3].title").value("TestTitle4"))
                .andExpect(jsonPath("$[3].category").value("TestCategory4"))
                .andExpect(jsonPath("$[3].content").value("TestContent4"))
                .andExpect(jsonPath("$[3].tags").value(hasSize(1)))
                .andExpect(jsonPath("$[3].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[3].createdAt").value("CreatedAt4"))
                .andExpect(jsonPath("$[3].remind").value("Remind4"))
                .andExpect(jsonPath("$[4].title").value("TestTitle5"))
                .andExpect(jsonPath("$[4].category").value("TestCategory5"))
                .andExpect(jsonPath("$[4].content").value("TestContent5"))
                .andExpect(jsonPath("$[4].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[4].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[4].createdAt").value("CreatedAt5"))
                .andExpect(jsonPath("$[4].remind").value("Remind5"));
        Mockito.verify(noteService, Mockito.times(1)).findByAnyTag(Mockito.anySet());
    }

    @Test
    void findNotesByCategorySuccessTest() throws Exception {
        final List<NoteDTO> noteDTOS = List.of(
                new NoteDTO("TestTitle1", "TestCategory1", "TestContent1", Set.of("TestTag1", "TestTag2"), "CreatedAt1", "Remind1"),
                new NoteDTO("TestTitle2", "TestCategory1", "TestContent2", Set.of("TestTag1", "TestTag3"), "CreatedAt2", "Remind2"),
                new NoteDTO("TestTitle3", "TestCategory1", "TestContent3", Set.of("TestTag7", "TestTag8"), "CreatedAt3", "Remind3"),
                new NoteDTO("TestTitle4", "TestCategory1", "TestContent4", Set.of("TestTag1", "TestTag2", "TestTag10"), "CreatedAt4", "Remind4")
        );
        Mockito.when(noteService.findByCategory(Mockito.any(Category.class))).thenReturn(noteDTOS);
        mockMvc.perform(get("/notes/s/category/{categoryName}", "TestCategory1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(noteDTOS.size()))
                .andExpect(jsonPath("$[0].title").value("TestTitle1"))
                .andExpect(jsonPath("$[0].category").value("TestCategory1"))
                .andExpect(jsonPath("$[0].content").value("TestContent1"))
                .andExpect(jsonPath("$[0].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[0].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[0].createdAt").value("CreatedAt1"))
                .andExpect(jsonPath("$[0].remind").value("Remind1"))
                .andExpect(jsonPath("$[1].title").value("TestTitle2"))
                .andExpect(jsonPath("$[1].category").value("TestCategory1"))
                .andExpect(jsonPath("$[1].content").value("TestContent2"))
                .andExpect(jsonPath("$[1].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[1].tags").value(hasItem("TestTag3")))
                .andExpect(jsonPath("$[1].createdAt").value("CreatedAt2"))
                .andExpect(jsonPath("$[1].remind").value("Remind2"))
                .andExpect(jsonPath("$[2].title").value("TestTitle3"))
                .andExpect(jsonPath("$[2].category").value("TestCategory1"))
                .andExpect(jsonPath("$[2].content").value("TestContent3"))
                .andExpect(jsonPath("$[2].tags").value(hasSize(2)))
                .andExpect(jsonPath("$[2].tags").value(hasItem("TestTag7")))
                .andExpect(jsonPath("$[2].tags").value(hasItem("TestTag8")))
                .andExpect(jsonPath("$[2].createdAt").value("CreatedAt3"))
                .andExpect(jsonPath("$[2].remind").value("Remind3"))
                .andExpect(jsonPath("$[3].title").value("TestTitle4"))
                .andExpect(jsonPath("$[3].category").value("TestCategory1"))
                .andExpect(jsonPath("$[3].content").value("TestContent4"))
                .andExpect(jsonPath("$[3].tags").value(hasSize(3)))
                .andExpect(jsonPath("$[3].tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$[3].tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$[3].tags").value(hasItem("TestTag10")))
                .andExpect(jsonPath("$[3].createdAt").value("CreatedAt4"))
                .andExpect(jsonPath("$[3].remind").value("Remind4"));
        Mockito.verify(noteService, Mockito.times(1)).findByCategory(Mockito.any(Category.class));
    }

    @Test
    void deleteNotesSuccessTest() throws Exception {
        final Date createdAt = new Date();
        final Date remind = new Date();
        final NoteDTO noteDTO = new NoteDTO(
                "TestTitle1",
                "TestCategory1",
                "TestContent1",
                Set.of(
                        "TestTag1",
                        "TestTag2"
                ),
                createdAt.toString(),
                remind.toString()
        );
        final CreateNoteRequest request = new CreateNoteRequest(
                noteDTO.category,
                noteDTO.content,
                noteDTO.tags,
                remind.getTime()
        );
        Mockito.when(noteService.createNote(Mockito.any(Category.class), Mockito.anyString(), Mockito.anySet(), Mockito.any()))
                .thenReturn(noteDTO);

        mockMvc.perform(post("/notes").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TestTitle1"))
                .andExpect(jsonPath("$.category").value("TestCategory1"))
                .andExpect(jsonPath("$.content").value("TestContent1"))
                .andExpect(jsonPath("$.tags").value(hasSize(2)))
                .andExpect(jsonPath("$.tags").value(hasItem("TestTag1")))
                .andExpect(jsonPath("$.tags").value(hasItem("TestTag2")))
                .andExpect(jsonPath("$.createdAt").value(createdAt.toString()))
                .andExpect(jsonPath("$.remind").value(noteDTO.remind));
        Mockito.verify(noteService, Mockito.times(1))
                .createNote(Mockito.any(Category.class), Mockito.anyString(), Mockito.anySet(), Mockito.any());
        final DeleteNoteRequest deleteRequest = new DeleteNoteRequest("TestCategory1", "TestContent1", createdAt.getTime());
        mockMvc.perform(delete("/notes/delete").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isNoContent());
        Mockito.verify(noteService, Mockito.times(1)).deleteNote(Mockito.any(Category.class), Mockito.anyString(), Mockito.any());
    }
}