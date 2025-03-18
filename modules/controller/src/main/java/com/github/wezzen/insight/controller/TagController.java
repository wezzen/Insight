package com.github.wezzen.insight.controller;

import com.github.wezzen.insight.dto.response.TagDTO;
import com.github.wezzen.insight.service.TagService;
import com.github.wezzen.insight.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(final TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagDTO> createTag(@RequestBody final Tag tag) {
        final TagDTO created = tagService.createTag(tag.getTag());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> getTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteTag(@PathVariable("name") final String tagName) {
        tagService.deleteTag(tagName);
        return ResponseEntity.noContent().build();
    }
}
