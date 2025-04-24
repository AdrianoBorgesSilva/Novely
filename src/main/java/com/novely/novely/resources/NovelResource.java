package com.novely.novely.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.novely.novely.domain.Novel;
import com.novely.novely.domain.User;
import com.novely.novely.dto.NovelCreateDTO;
import com.novely.novely.dto.NovelDTO;
import com.novely.novely.dto.NovelUpdateDTO;
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

    @PostMapping(value = "/create")
    public ResponseEntity<Void> createNovel(@RequestBody @Valid NovelCreateDTO novelCreateDTO, Authentication authentication){
        
        User user = userService.findByEmail(authentication.getName());
        Novel novel = novelService.createNovel(novelCreateDTO, user.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novel.getId()).toUri();

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

    @PostMapping("/{novelId}/view")
    public ResponseEntity<Void> addView(@PathVariable String novelId) {
        novelService.addView(novelId);
        return ResponseEntity.ok().build();
    }
}
