package com.github.wezzen.insight.controller;

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
    public ResponseEntity<Tag> createTag(@RequestBody final Tag tag) {
        final Tag created = tagService.createTag(tag.getTag());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteTag(@PathVariable final String name) {
        tagService.deleteTag(name);
        return ResponseEntity.noContent().build();
    }
}
