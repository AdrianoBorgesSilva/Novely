package com.novely.novely.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novely.novely.domain.Rating;
import com.novely.novely.domain.User;
import com.novely.novely.dto.RatingDTO;
import com.novely.novely.security.SecurityConfig;
import com.novely.novely.service.RatingService;
import com.novely.novely.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping(path = "/ratings")
@SecurityRequirement(name = SecurityConfig.SECURITY)
@Tag(name = "Avaliações", description = "Controller responsável pelas ações relacionadas as avaliações")
public class RatingResource {
    
    private final RatingService ratingService;
    private final UserService userService;
    
    public RatingResource(RatingService ratingService, UserService userService) {
        this.ratingService = ratingService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as avaliações", description = "Retorna todas as avaliações cadastradas no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avaliações retornadas com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<List<RatingDTO>> findAll() {
        List<RatingDTO> list = ratingService.findAll().stream().map(RatingDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{ratingId}")
    @Operation(summary = "Buscar avaliação por ID", description = "Retorna os dados da avaliação especificada por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avaliação retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<RatingDTO> findById(@PathVariable String ratingId) {
        Rating rating = ratingService.findById(ratingId);
        return ResponseEntity.ok().body(new RatingDTO(rating));
    }   

    @DeleteMapping(value = "/{ratingId}")
    @Operation(summary = "Deletar uma avaliação", description = "Deleta uma avaliação e remove sua referência na Novel associada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Avaliação deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<Void> deleteRating(@PathVariable String ratingId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName()); 
        ratingService.deleteRating(ratingId, user.getId());
        
        return ResponseEntity.noContent().build();
    }
}
