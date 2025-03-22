package com.github.wezzen.insight.controller;

import com.github.wezzen.insight.dto.response.TagDTO;
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
        final List<TagDTO> tagsDTOS = List.of(
                new TagDTO("TestTag1"),
                new TagDTO("TestTag2"),
                new TagDTO("TestTag3")
        );

        Mockito.when(tagService.getAllTags()).thenReturn(tagsDTOS);

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(tagsDTOS.size()))
                .andExpect(jsonPath("$[0].tag").value("TestTag1"))
                .andExpect(jsonPath("$[1].tag").value("TestTag2"))
                .andExpect(jsonPath("$[2].tag").value("TestTag3"));

        Mockito.verify(tagService, Mockito.times(1)).getAllTags();
    }

    @Test
    void createTagTest() throws Exception {
        final TagDTO tag = new TagDTO("TestTag1");
        Mockito.when(tagService.createTag(Mockito.anyString())).thenReturn(tag);

        mockMvc.perform(post("/tags").contentType(MediaType.APPLICATION_JSON_VALUE).content(tag.tag))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tag").value(tag.tag));

        Mockito.verify(tagService, Mockito.times(1)).createTag(tag.tag);
    }

    @Test
    void deleteTagTest() throws Exception {
        final TagDTO tagDTO = new TagDTO("TestTag1");
        Mockito.when(tagService.createTag(Mockito.anyString())).thenReturn(tagDTO);

        mockMvc.perform(post("/tags").contentType(MediaType.APPLICATION_JSON_VALUE).content(tagDTO.tag))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tag").value(tagDTO.tag));

        Mockito.verify(tagService, Mockito.times(1)).createTag(tagDTO.tag);

        mockMvc.perform(delete("/tags/{tag}", tagDTO.tag)).andExpect(status().isNoContent());
        Mockito.verify(tagService, Mockito.times(1)).deleteTag(tagDTO.tag);
    }

}