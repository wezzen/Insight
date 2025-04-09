package com.github.wezzen.insight.service;

import com.github.wezzen.insight.dto.request.CreateTagRequest;
import com.github.wezzen.insight.dto.response.TagResponse;
import com.github.wezzen.insight.model.Tag;
import com.github.wezzen.insight.repository.TagRepository;
import com.github.wezzen.insight.service.exception.DuplicateTagException;
import com.github.wezzen.insight.service.exception.DeletingTagWithNotesException;
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
    public TagResponse createTag(final CreateTagRequest request) {
        if (tagRepository.findById(request.tag).isPresent()) {
            throw new DuplicateTagException(String.format("Tag [%s] already exists", request.tag));
        }
        return convert(tagRepository.save(new Tag(request.tag, colorGenerator.generateSoftColor())));
    }

    public List<TagResponse> getAllTags() {
        return StreamSupport.stream(tagRepository.findAll().spliterator(), false).map(this::convert).toList();
    }

    @Transactional
    public void deleteTag(final String tag) {
        final Optional<Tag> optional = tagRepository.findById(tag);
        if (optional.isEmpty()) {
            throw new TagNotFoundException(String.format("Tag [%s] is not found", tag));
        }
        final Tag fetched = optional.get();
        if (fetched.getNotes() != null && !fetched.getNotes().isEmpty()) {
            throw new DeletingTagWithNotesException(String.format("Tag [%s] has [%d] notes", fetched, fetched.getNotes().size()));
        }
        tagRepository.deleteById(tag);
    }

    protected TagResponse convert(final Tag tag) {
        return new TagResponse(tag.getTag(), tag.getColor());
    }
}
