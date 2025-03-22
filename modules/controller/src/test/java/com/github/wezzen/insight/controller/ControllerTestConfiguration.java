package com.github.wezzen.insight.controller;

import com.github.wezzen.insight.service.CategoryService;
import com.github.wezzen.insight.service.NoteService;
import com.github.wezzen.insight.service.TagService;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@SpringBootConfiguration
@Import({CategoryController.class, TagController.class, NoteController.class})
public class ControllerTestConfiguration {

    @Bean
    @Primary
    public CategoryService categoryService() {
        return Mockito.mock(CategoryService.class);
    }

    @Bean
    @Primary
    public TagService tagService() {
        return Mockito.mock(TagService.class);
    }

    @Bean
    @Primary
    public NoteService noteService() {
        return Mockito.mock(NoteService.class);
    }

}
