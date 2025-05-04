package com.novely.novely.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novely.novely.domain.Chapter;
import com.novely.novely.domain.User;
import com.novely.novely.dto.ChapterDTO;
import com.novely.novely.dto.ChapterUpdateDTO;
import com.novely.novely.service.ChapterService;
import com.novely.novely.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/chapters")
public class ChapterResource {
    
    private final ChapterService chapterService;
    private final UserService userService;
    
    public ChapterResource(ChapterService chapterService, UserService userService) {
        this.chapterService = chapterService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ChapterDTO>> findAll() {
        List<ChapterDTO> list = chapterService.findAll().stream().map(ChapterDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{chapterId}")
    public ResponseEntity<ChapterDTO> findById(@PathVariable String chapterId) {
        Chapter chapter = chapterService.findById(chapterId);
        return ResponseEntity.ok().body(new ChapterDTO(chapter));
    }

    @PutMapping(value = "/{chapterId}")
    public ResponseEntity<Void> updateChapter(@RequestBody @Valid ChapterUpdateDTO chapterUpdateDTO, @PathVariable String chapterId, Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());
        chapterService.updateChapter(chapterUpdateDTO, chapterId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{chapterId}")
    public ResponseEntity<Void> deleteChapter(@PathVariable String chapterId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        chapterService.deleteChapter(chapterId, user.getId());

        return ResponseEntity.noContent().build();
    }
}
