package com.github.wezzen.insight.service;

import com.github.wezzen.insight.dto.response.TagDTO;
import com.github.wezzen.insight.model.Note;
import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.repository.TagRepository;
import com.github.wezzen.insight.service.exception.DuplicateTagException;
import com.github.wezzen.insight.service.exception.TagNotFoundException;
import com.github.wezzen.insight.utils.ColorGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TagServiceTest {

    private final TagRepository tagRepository = Mockito.mock(TagRepository.class);

    private final ColorGenerator colorGenerator = Mockito.mock(ColorGenerator.class);

    private final TagService tagService = new TagService(tagRepository, colorGenerator);

    @Test
    void convertTest() {
        final String color = colorGenerator.generateSoftColor();
        final Tag tag = new Tag("TesTag", color);
        final TagDTO dto = tagService.convert(tag);
        assertEquals(tag.getTag(), dto.tag);
        assertEquals(tag.getColor(), dto.color);
    }

    @Test
    void createTagSuccessTest() {
        final Tag tag = new Tag("Test Tag", "RED");
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.empty());
        Mockito.when(tagRepository.save(tag)).thenReturn(tag);
        final TagDTO createdTag = tagService.createTag(tag.getTag());

        assertNotNull(createdTag);
        assertEquals(tag.getTag(), createdTag.tag);
        assertEquals(tag.getColor(), createdTag.color);
        Mockito.verify(tagRepository, Mockito.times(1)).save(tag);
    }

    @Test
    void createDuplicateTagFailedTest() {
        final Tag tag1 = new Tag("Test Tag", "RED");
        final Tag tag2 = new Tag("Test Tag", "BLACK");
        Mockito.when(tagRepository.findById(tag1.getTag())).thenReturn(Optional.of(tag1));
        assertThrows(DuplicateTagException.class, () -> tagService.createTag(tag1.getTag()));
        assertThrows(DuplicateTagException.class, () -> tagService.createTag(tag2.getTag()));
        Mockito.verify(tagRepository, Mockito.times(0)).save(tag2);
    }

    @Test
    void getTagsSuccessTest() {
        final List<Tag> tags = List.of(
                new Tag("Test Tag1", "RED"),
                new Tag("Test Tag2", "BLACK"),
                new Tag("Test Tag3", "YELLOW")
        );
        Mockito.when(tagRepository.findAll()).thenReturn(tags);
        final List<TagDTO> fetchedTags = tagService.getAllTags();
        assertNotNull(fetchedTags);
        assertEquals(tags.size(), fetchedTags.size());
        assertEquals(tags.get(0).getTag(), fetchedTags.get(0).tag);
        assertEquals(tags.get(0).getColor(), fetchedTags.get(0).color);
        assertEquals(tags.get(1).getTag(), fetchedTags.get(1).tag);
        assertEquals(tags.get(1).getColor(), fetchedTags.get(1).color);
        assertEquals(tags.get(2).getTag(), fetchedTags.get(2).tag);
        assertEquals(tags.get(2).getColor(), fetchedTags.get(2).color);
        Mockito.verify(tagRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deleteTagSuccessTest() {
        final Tag tag = new Tag("Test Tag", "RED");
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.of(tag));
        tagService.deleteTag(tag.getTag());
        Mockito.verify(tagRepository, Mockito.times(1)).deleteById(tag.getTag());
    }

    @Test
    void deleteTagFailedTest() {
        final Tag tag = new Tag("Test Tag", "RED");
        Mockito.when(tagRepository.findById(tag.getTag())).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(tag.getTag()));
        Mockito.verify(tagRepository, Mockito.times(0)).deleteById(tag.getTag());
    }
}