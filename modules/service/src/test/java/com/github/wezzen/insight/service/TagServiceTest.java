package com.github.wezzen.insight.service;

import com.github.wezzen.insight.dto.response.TagDTO;
import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.repository.TagRepository;
import com.github.wezzen.insight.service.exception.DuplicateTagException;
import com.github.wezzen.insight.service.exception.TagNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TagServiceTest {

    private final TagRepository tagRepository = Mockito.mock(TagRepository.class);

    private final TagService tagService = new TagService(tagRepository);

    @Test
    void convertTest() {
        final Tag tag = new Tag("TesTag");
        final TagDTO dto = tagService.convert(tag);
        assertEquals(tag.getTag(), dto.tag);
    }

    @Test
    void createTagSuccessTest() {
        final Tag tag = new Tag("Test Tag");
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.empty());
        Mockito.when(tagRepository.save(tag)).thenReturn(tag);
        final TagDTO createdTag = tagService.createTag(tag.getTag());

        assertNotNull(createdTag);
        assertEquals(tag.getTag(), createdTag.tag);
        Mockito.verify(tagRepository, Mockito.times(1)).save(tag);
    }

    @Test
    void createTagFailedTest() {
        final Tag tag = new Tag("Test Tag");
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.of(tag));
        assertThrows(DuplicateTagException.class, () -> tagService.createTag(tag.getTag()));
        Mockito.verify(tagRepository, Mockito.times(0)).save(tag);
    }

    @Test
    void getTagsSuccessTest() {
        final List<Tag> tags = List.of(
                new Tag("Test Tag1"),
                new Tag("Test Tag2"),
                new Tag("Test Tag3")
        );
        Mockito.when(tagRepository.findAll()).thenReturn(tags);
        final List<TagDTO> fetchedTags = tagService.getAllTags();
        assertNotNull(fetchedTags);
        assertEquals(tags.size(), fetchedTags.size());
        assertEquals(tags.get(0).getTag(), fetchedTags.get(0).tag);
        assertEquals(tags.get(1).getTag(), fetchedTags.get(1).tag);
        assertEquals(tags.get(2).getTag(), fetchedTags.get(2).tag);
        Mockito.verify(tagRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deleteTagSuccessTest() {
        final Tag tag = new Tag("Test Tag");
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.of(tag));
        tagService.deleteTag(tag.getTag());
        Mockito.verify(tagRepository, Mockito.times(1)).deleteById(tag.getTag());
    }

    @Test
    void deleteTagFailedTest() {
        final Tag tag = new Tag("Test Tag");
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(tag.getTag()));
        Mockito.verify(tagRepository, Mockito.times(0)).deleteById(tag.getTag());
    }
}