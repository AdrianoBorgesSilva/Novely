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
import com.novely.novely.security.SecurityConfig;
import com.novely.novely.service.CommentService;
import com.novely.novely.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/comments")
@SecurityRequirement(name = SecurityConfig.SECURITY)
@Tag(name = "Comments", description = "Controller responsible for actions related to comments")
public class CommentResource {
    
    private final CommentService commentService;
    private final UserService userService;

    public CommentResource(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "List all comments", description = "Returns all comments registered in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comments returned successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CommentDTO>> findAll() {
       List<CommentDTO> list = commentService.findAll().stream().map(CommentDTO::new).collect(Collectors.toList());
       return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{commentId}")
    @Operation(summary = "Return a single comment", description = "Returns the comment data specified by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comment returned successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CommentDTO> findById(@PathVariable String commentId) {
        Comment comment = commentService.findById(commentId);
        return ResponseEntity.ok().body(new CommentDTO(comment));
    } 

    @DeleteMapping(value = "/{commentId}")
    @Operation(summary = "Delete a comment", description = "Deletes a comment and removes its reference in the associated Novel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId, Authentication authentication) { 
        
        User user = userService.findByEmail(authentication.getName());
        commentService.deleteComment(commentId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{commentId}/like")
    @Operation(summary = "Like a comment", description = "Adds a like to the specified comment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comment liked successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> likeComment(@PathVariable String commentId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        commentService.likeComment(commentId, user.getId());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{commentId}/dislike")
    @Operation(summary = "Dislike a comment", description = "Adds a dislike to the specified comment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comment successfully dislike"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> dislikeComment(@PathVariable String commentId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        commentService.dislikeComment(commentId, user.getId());
        
        return ResponseEntity.ok().build();
    }
}
