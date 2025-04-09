package com.github.wezzen.insight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wezzen.insight.dto.request.CreateTagRequest;
import com.github.wezzen.insight.dto.response.TagResponse;
import com.github.wezzen.insight.service.TagService;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
@ContextConfiguration(classes = ControllerTestConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TagService tagService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.reset(tagService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllTagsEmptyTest() throws Exception {
        Mockito.when(tagService.getAllTags()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getTagsTest() throws Exception {
        final List<TagResponse> tagsDTOS = List.of(
                new TagResponse("TestTag1", "RED"),
                new TagResponse("TestTag2", "BLACK"),
                new TagResponse("TestTag3", "ORANGE")
        );

        Mockito.when(tagService.getAllTags()).thenReturn(tagsDTOS);

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(tagsDTOS.size()))
                .andExpect(jsonPath("$[0].tag").value(tagsDTOS.getFirst().tag))
                .andExpect(jsonPath("$[1].tag").value(tagsDTOS.get(1).tag))
                .andExpect(jsonPath("$[2].tag").value(tagsDTOS.get(2).tag));

        Mockito.verify(tagService, Mockito.times(1)).getAllTags();
    }

    @Test
    void createTagTest() throws Exception {
        final TagResponse tagResponse = new TagResponse("TestTag1", "RED");
        final CreateTagRequest request = new CreateTagRequest(tagResponse.tag);
        Mockito.when(tagService.createTag(request)).thenReturn(tagResponse);

        mockMvc.perform(post("/tags").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tag").value(tagResponse.tag));

        Mockito.verify(tagService, Mockito.times(1)).createTag(request);
    }

    @Test
    void deleteTagTest() throws Exception {
        final TagResponse tagResponse = new TagResponse("TestTag1", "RED");
        final CreateTagRequest request = new CreateTagRequest(tagResponse.tag);
        Mockito.when(tagService.createTag(request)).thenReturn(tagResponse);

        mockMvc.perform(post("/tags").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tag").value(tagResponse.tag));

        Mockito.verify(tagService, Mockito.times(1)).createTag(request);

        mockMvc.perform(delete("/tags/{tag}", tagResponse.tag)).andExpect(status().isNoContent());
        Mockito.verify(tagService, Mockito.times(1)).deleteTag(tagResponse.tag);
    }

}