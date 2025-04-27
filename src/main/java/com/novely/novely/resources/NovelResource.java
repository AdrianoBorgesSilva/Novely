package com.novely.novely.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.novely.novely.domain.Chapter;
import com.novely.novely.domain.Comment;
import com.novely.novely.domain.Novel;
import com.novely.novely.domain.Rating;
import com.novely.novely.domain.User;
import com.novely.novely.dto.ChapterCreateDTO;
import com.novely.novely.dto.ChapterDTO;
import com.novely.novely.dto.CommentCreateDTO;
import com.novely.novely.dto.CommentDTO;
import com.novely.novely.dto.NovelCreateDTO;
import com.novely.novely.dto.NovelDTO;
import com.novely.novely.dto.NovelUpdateDTO;
import com.novely.novely.dto.RatingCreateDTO;
import com.novely.novely.dto.RatingDTO;
import com.novely.novely.service.NovelService;
import com.novely.novely.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/novels")
public class NovelResource {
    
    @Autowired
    NovelService novelService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<NovelDTO>> findAll() {
        List<NovelDTO> list = novelService.findAll().stream().map(NovelDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{novelId}")
    public ResponseEntity<NovelDTO> findById(@PathVariable String novelId) {
        Novel novel = novelService.findById(novelId);
        return ResponseEntity.ok().body(new NovelDTO(novel));
    }

    @GetMapping(value = "/{novelId}/chapters")
    public ResponseEntity<List<ChapterDTO>> findChaptersFromNovel(@PathVariable String novelId) {
        List<ChapterDTO> list = novelService.findChaptersFromNovel(novelId).stream().map(ChapterDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{novelId}/comments")
    public ResponseEntity<List<CommentDTO>> findCommentsFromNovel(@PathVariable String novelId) {
        List<CommentDTO> list = novelService.findCommentsFromNovel(novelId).stream().map(CommentDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{novelId}/ratings")
    public ResponseEntity<List<RatingDTO>> findRatingsFromNovel(@PathVariable String novelId) {
        List<RatingDTO> list = novelService.findRatingsFromNovel(novelId).stream().map(RatingDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Void> createNovel(@RequestBody @Valid NovelCreateDTO novelCreateDTO, Authentication authentication){
        
        User user = userService.findByEmail(authentication.getName());
        Novel novel = novelService.createNovel(novelCreateDTO, user.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novel.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping(value = "/{novelId}/chapters")
    public ResponseEntity<Void> createChapter(@RequestBody @Valid ChapterCreateDTO chapterDTO, @PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        Chapter chapter = novelService.createChapter(chapterDTO, novelId, user.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(chapter.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping(value = "/{novelId}/comments")
    public ResponseEntity<Void> createComment(@RequestBody @Valid CommentCreateDTO commentCreateDTO, @PathVariable String novelId, Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());
        Comment comment = novelService.createComment(commentCreateDTO, user.getId(), novelId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.getId()).toUri();
        
        return ResponseEntity.created(uri).build();
    }

    @PostMapping(value = "/{novelId}/ratings")
    public ResponseEntity<Void> createRating(@RequestBody @Valid RatingCreateDTO ratingCreateDTO, @PathVariable String novelId, Authentication authentication){

        User user = userService.findByEmail(authentication.getName());
        Rating rating = novelService.createRating(ratingCreateDTO, novelId, user.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(rating.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{novelId}")
    public ResponseEntity<Void> updateNovel(@RequestBody @Valid NovelUpdateDTO novelUpdateDTO, @PathVariable String novelId, Authentication authentication){
        
        User user = userService.findByEmail(authentication.getName());
        novelService.updateNovel(novelUpdateDTO, novelId, user.getId());
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{novelId}")
    public ResponseEntity<Void> deleteNovel(@PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        novelService.deleteNovel(novelId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{novelId}/views")
    public ResponseEntity<Void> incrementViews(@PathVariable String novelId) {
        novelService.incrementViews(novelId);
        return ResponseEntity.ok().build();
    }
}
