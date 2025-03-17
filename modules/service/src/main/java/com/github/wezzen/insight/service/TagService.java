package com.github.wezzen.insight.service;

import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(final TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Tag createTag(final String tag) {
        if (tagRepository.findById(tag).isPresent()) {
            throw new RuntimeException("Tag " + tag + " already exists");
        }
        return tagRepository.save(new Tag(tag));
    }

    public List<Tag> getAllTags() {
        return (List<Tag>) tagRepository.findAll();
    }

    @Transactional
    public void deleteTag(final String tag) {
        tagRepository.deleteById(tag);
    }
}
