package com.github.wezzen.insight.service;

import com.github.wezzen.insight.dto.response.TagDTO;
import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.repository.TagRepository;
import com.github.wezzen.insight.service.exception.DuplicateTagException;
import com.github.wezzen.insight.service.exception.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(final TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public TagDTO createTag(final String tag) {
        if (tagRepository.findById(tag).isPresent()) {
            throw new DuplicateTagException("Tag " + tag + " already exists");
        }
        final Tag saved = tagRepository.save(new Tag(tag));
        return new TagDTO(saved.getTag());
    }

    public List<TagDTO> getAllTags() {
        final List<TagDTO> dtos = new ArrayList<>();
        for (final Tag tag : tagRepository.findAll()) {
            dtos.add(new TagDTO(tag.getTag()));
        }
        return dtos;
    }

    @Transactional
    public void deleteTag(final String tag) {
        if (tagRepository.findById(tag).isEmpty()) {
            throw new TagNotFoundException("Tag " + tag + " not found");
        }
        tagRepository.deleteById(tag);
    }
}
