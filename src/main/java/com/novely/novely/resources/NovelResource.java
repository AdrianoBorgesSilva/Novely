package com.novely.novely.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.novely.novely.security.SecurityConfig;
import com.novely.novely.service.ChapterService;
import com.novely.novely.service.CommentService;
import com.novely.novely.service.CreateNovelService;
import com.novely.novely.service.DeleteNovelService;
import com.novely.novely.service.NovelService;
import com.novely.novely.service.RatingService;
import com.novely.novely.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/novels")
@SecurityRequirement(name = SecurityConfig.SECURITY)
@Tag(name = "Novels", description = "Controller responsável pelas ações relacionadas as Novels")
public class NovelResource {

    private final UserService userService;
    private final NovelService novelService;
    private final ChapterService chapterService;
    private final CommentService commentService;
    private final RatingService ratingService;
    private final DeleteNovelService deleteNovelService;
    private final CreateNovelService createNovelService;

    public NovelResource(UserService userService, NovelService novelService, ChapterService chapterService, CommentService commentService, RatingService ratingService, DeleteNovelService deleteNovelService, CreateNovelService createNovelService){
        this.userService = userService;
        this.novelService = novelService;
        this.chapterService = chapterService;
        this.commentService = commentService;
        this.ratingService = ratingService;
        this.deleteNovelService = deleteNovelService;
        this.createNovelService = createNovelService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as Novels", description = "Retorna uma lista contendo todas as Novels")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Novels retornadas com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<List<NovelDTO>> findAll() {
        List<NovelDTO> list = novelService.findAll().stream().map(NovelDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{novelId}")
    @Operation(summary = "Retornar uma única Novel", description = "Retorna uma Novel correspondente ao ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Novel retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Novel não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<NovelDTO> findById(@PathVariable String novelId) {
        Novel novel = novelService.findById(novelId);
        return ResponseEntity.ok().body(new NovelDTO(novel));
    }

    @GetMapping(value = "/{novelId}/chapters")
    @Operation(summary = "Listar capítulos de uma Novel", description = "Retorna todos os capítulos da Novel especificada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Capítulos retornados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Novel não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<List<ChapterDTO>> findChaptersFromNovel(@PathVariable String novelId) {
        List<ChapterDTO> list = chapterService.findByNovelId(novelId).stream().map(ChapterDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{novelId}/comments")
    @Operation(summary = "Listar comentários de uma Novel", description = "Retorna todos os comentários da Novel especificada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comentários retornados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Novel não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<List<CommentDTO>> findCommentsFromNovel(@PathVariable String novelId) {
        List<CommentDTO> list = commentService.findByNovelId(novelId).stream().map(CommentDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{novelId}/ratings")
    @Operation(summary = "Listar avaliações de uma Novel", description = "Retorna todas as avaliações da Novel especificada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avaliações retornadas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Novel não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<List<RatingDTO>> findRatingsFromNovel(@PathVariable String novelId) {
        List<RatingDTO> list = ratingService.findByNovelId(novelId).stream().map(RatingDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    @Operation(summary = "Criar uma Novel", description = "Cria uma nova Novel associada ao usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Novel criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou erro de validação"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> createNovel(@RequestBody @Valid NovelCreateDTO novelCreateDTO, Authentication authentication){
        
        User user = userService.findByEmail(authentication.getName());
        Novel novel = createNovelService.createNovel(novelCreateDTO, user.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novel.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping(value = "/{novelId}/chapters")
    @Operation(summary = "Criar um capítulo", description = "Cria um novo capítulo associado à Novel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Capítulo criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou erro de validação"),
        @ApiResponse(responseCode = "404", description = "Novel não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> createChapter(@RequestBody @Valid ChapterCreateDTO chapterDTO, @PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        Chapter chapter = chapterService.createChapter(chapterDTO, novelId, user.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(chapter.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping(value = "/{novelId}/comments")
    @Operation(summary = "Criar um comentário", description = "Cria um novo comentário associado à Novel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Comentário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou erro de validação"),
        @ApiResponse(responseCode = "404", description = "Novel não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> createComment(@RequestBody @Valid CommentCreateDTO commentCreateDTO, @PathVariable String novelId, Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());
        Comment comment = commentService.createComment(commentCreateDTO, user.getId(), novelId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.getId()).toUri();
        
        return ResponseEntity.created(uri).build();
    }

    @PostMapping(value = "/{novelId}/ratings")
    @Operation(summary = "Criar uma avaliação", description = "Cria uma nova avaliação associada à Novel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou erro de validação"),
        @ApiResponse(responseCode = "404", description = "Novel não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> createRating(@RequestBody @Valid RatingCreateDTO ratingCreateDTO, @PathVariable String novelId, Authentication authentication){

        User user = userService.findByEmail(authentication.getName());
        Rating rating = ratingService.createRating(ratingCreateDTO, novelId, user.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(rating.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping(value = "/{novelId}")
    @Operation(summary = "Atualizar Novel", description = "Atualização de uma Novel existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Novel atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Novel não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> updateNovel(@RequestBody @Valid NovelUpdateDTO novelUpdateDTO, @PathVariable String novelId, Authentication authentication){
        
        User user = userService.findByEmail(authentication.getName());
        novelService.updateNovel(novelUpdateDTO, novelId, user.getId());
        
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{novelId}")
    @Operation(summary = "Deletar uma Novel", description = "Deleta uma novel e todos os dados relacionados a ela (como capítulos, comentários, avaliações, etc.)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Novel deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Novel não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> deleteNovel(@PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        deleteNovelService.deleteNovel(novelId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{novelId}/views")
    @Operation(summary = "Incrementar visualização", description = "Incrementa o contador de visualizações da novel especificada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Visualização incrementada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Novel não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> incrementViews(@PathVariable String novelId) {
        novelService.incrementViews(novelId);
        return ResponseEntity.ok().build();
    }
}
