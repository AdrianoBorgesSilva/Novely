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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.novely.novely.domain.Comment;
import com.novely.novely.domain.User;
import com.novely.novely.dto.CommentCreateDTO;
import com.novely.novely.dto.CommentDTO;
import com.novely.novely.service.CommentService;
import com.novely.novely.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/comments")
public class CommentResource {
    
    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> findAll() {
       List<CommentDTO> list = commentService.findAll().stream().map(CommentDTO::new).collect(Collectors.toList());
       return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{commentId}")
    public ResponseEntity<CommentDTO> findById(@PathVariable String commentId) {
        Comment comment = commentService.findById(commentId);
        return ResponseEntity.ok().body(new CommentDTO(comment));
    } 
    
    @PostMapping(value = "/novels/{novelId}")
    public ResponseEntity<Void> createComment(@RequestBody @Valid CommentCreateDTO commentCreateDTO, @PathVariable String novelId, Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());
        Comment comment = commentService.createComment(commentCreateDTO, user.getId(), novelId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.getId()).toUri();
        
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId, Authentication authentication) { 
        
        User user = userService.findByEmail(authentication.getName());
        commentService.deleteComment(commentId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable String commentId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        commentService.likeComment(commentId, user.getId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{commentId}/dislike")
    public ResponseEntity<Void> dislikeComment(@PathVariable String commentId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        commentService.dislikeComment(commentId, user.getId());
        
        return ResponseEntity.ok().build();
    }
}
