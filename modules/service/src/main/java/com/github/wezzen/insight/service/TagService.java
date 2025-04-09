package com.github.wezzen.insight.service;

import com.github.wezzen.insight.dto.response.TagDTO;
import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.repository.TagRepository;
import com.github.wezzen.insight.service.exception.DuplicateTagException;
import com.github.wezzen.insight.service.exception.TagHasNotesException;
import com.github.wezzen.insight.service.exception.TagNotFoundException;
import com.github.wezzen.insight.utils.ColorGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class TagService {

    private final TagRepository tagRepository;

    private final ColorGenerator colorGenerator;

    @Autowired
    public TagService(final TagRepository tagRepository, final ColorGenerator colorGenerator) {
        this.tagRepository = tagRepository;
        this.colorGenerator = colorGenerator;
    }

    @Transactional
    public TagDTO createTag(final String tag) {
        if (tagRepository.findById(tag).isPresent()) {
            throw new DuplicateTagException("Tag " + tag + " already exists");
        }
        return convert(tagRepository.save(new Tag(tag, colorGenerator.generateSoftColor())));
    }

    public List<TagDTO> getAllTags() {
        return StreamSupport.stream(tagRepository.findAll().spliterator(), false).map(this::convert).toList();
    }

    @Transactional
    public void deleteTag(final String tag) {
        final Optional<Tag> optional = tagRepository.findById(tag);
        if (optional.isEmpty()) {
            throw new TagNotFoundException("Tag " + optional + " not found");
        }
        final Tag fetched = optional.get();
        if (fetched.getNotes() != null && !fetched.getNotes().isEmpty()) {
            throw new TagHasNotesException("Tag " + fetched + " has " + fetched.getNotes().size() + " notes");
        }
        tagRepository.deleteById(tag);
    }

    protected TagDTO convert(final Tag tag) {
        return new TagDTO(tag.getTag(), tag.getColor());
    }
}
