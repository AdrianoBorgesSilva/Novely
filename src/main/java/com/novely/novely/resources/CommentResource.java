package com.novely.novely.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novely.novely.domain.Comment;
import com.novely.novely.domain.User;
import com.novely.novely.dto.CommentDTO;
import com.novely.novely.service.CommentService;
import com.novely.novely.service.UserService;

@RestController
@RequestMapping(path = "/comments")
public class CommentResource {
    
    private final CommentService commentService;
    private final UserService userService;

    public CommentResource(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

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

    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId, Authentication authentication) { 
        
        User user = userService.findByEmail(authentication.getName());
        commentService.deleteComment(commentId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable String commentId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        commentService.likeComment(commentId, user.getId());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{commentId}/dislike")
    public ResponseEntity<Void> dislikeComment(@PathVariable String commentId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        commentService.dislikeComment(commentId, user.getId());
        
        return ResponseEntity.ok().build();
    }
}
