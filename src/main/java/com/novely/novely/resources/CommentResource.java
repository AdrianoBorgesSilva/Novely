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
@Tag(name = "Comentários", description = "Controller responsável pelas ações relacionadas aos comentários")
public class CommentResource {
    
    private final CommentService commentService;
    private final UserService userService;

    public CommentResource(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os comentários", description = "Retorna todos os comentários cadastrados no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comentários retornados com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<List<CommentDTO>> findAll() {
       List<CommentDTO> list = commentService.findAll().stream().map(CommentDTO::new).collect(Collectors.toList());
       return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{commentId}")
    @Operation(summary = "Retornar um único comentário", description = "Retorna os dados do comentário especificado pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comentário retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<CommentDTO> findById(@PathVariable String commentId) {
        Comment comment = commentService.findById(commentId);
        return ResponseEntity.ok().body(new CommentDTO(comment));
    } 

    @DeleteMapping(value = "/{commentId}")
    @Operation(summary = "Deletar um comentário", description = "Deleta um comentário e remove sua referência na novel associada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Comentário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId, Authentication authentication) { 
        
        User user = userService.findByEmail(authentication.getName());
        commentService.deleteComment(commentId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{commentId}/like")
    @Operation(summary = "Curtir um comentário", description = "Adiciona uma curtida ao comentário especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comentário curtido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> likeComment(@PathVariable String commentId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        commentService.likeComment(commentId, user.getId());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{commentId}/dislike")
    @Operation(summary = "Registrar descurtida", description = "Adiciona uma descurtida (downvote) ao comentário especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comentário descurtido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> dislikeComment(@PathVariable String commentId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        commentService.dislikeComment(commentId, user.getId());
        
        return ResponseEntity.ok().build();
    }
}
