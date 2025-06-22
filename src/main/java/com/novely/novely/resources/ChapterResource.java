package com.novely.novely.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novely.novely.domain.Chapter;
import com.novely.novely.domain.User;
import com.novely.novely.dto.ChapterDTO;
import com.novely.novely.dto.ChapterUpdateDTO;
import com.novely.novely.security.SecurityConfig;
import com.novely.novely.service.ChapterService;
import com.novely.novely.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/chapters")
@SecurityRequirement(name = SecurityConfig.SECURITY)
@Tag(name = "Capítulos", description = "Controller responsável pelas ações relacionadas aos capítulos")
public class ChapterResource {
    
    private final ChapterService chapterService;
    private final UserService userService;
    
    public ChapterResource(ChapterService chapterService, UserService userService) {
        this.chapterService = chapterService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os capítulos", description = "Retorna todos os capítulos cadastrados no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Capítulos retornados com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<List<ChapterDTO>> findAll() {
        List<ChapterDTO> list = chapterService.findAll().stream().map(ChapterDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{chapterId}")
    @Operation(summary = "Retornar um único capítulo", description = "Retorna os dados do capítulo especificado pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Capítulo retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Capítulo não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<ChapterDTO> findById(@PathVariable String chapterId) {
        Chapter chapter = chapterService.findById(chapterId);
        return ResponseEntity.ok().body(new ChapterDTO(chapter));
    }

    @PatchMapping(value = "/{chapterId}")
    @Operation(summary = "Atualizar capítulo", description = "Atualização de um capítulo especificado pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Capítulo atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Capítulo não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> updateChapter(@RequestBody @Valid ChapterUpdateDTO chapterUpdateDTO, @PathVariable String chapterId, Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());
        chapterService.updateChapter(chapterUpdateDTO, chapterId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{chapterId}")
    @Operation(summary = "Deletar um capítulo", description = "Deleta um capítulo e remove sua referência na novel associada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Capítulo deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Capítulo não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> deleteChapter(@PathVariable String chapterId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        chapterService.deleteChapter(chapterId, user.getId());

        return ResponseEntity.noContent().build();
    }
}
