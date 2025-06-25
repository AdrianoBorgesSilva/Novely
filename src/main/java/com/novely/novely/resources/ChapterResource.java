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
@Tag(name = "Chapters", description = "Controller responsible for actions related to chapters")
public class ChapterResource {
    
    private final ChapterService chapterService;
    private final UserService userService;
    
    public ChapterResource(ChapterService chapterService, UserService userService) {
        this.chapterService = chapterService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "List all chapters", description = "Returns all chapters registered in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chapters returned successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ChapterDTO>> findAll() {
        List<ChapterDTO> list = chapterService.findAll().stream().map(ChapterDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{chapterId}")
    @Operation(summary = "Get a single chapter", description = "Returns the data for the chapter specified by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chapter returned successfully"),
        @ApiResponse(responseCode = "404", description = "Chapter not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ChapterDTO> findById(@PathVariable String chapterId) {
        Chapter chapter = chapterService.findById(chapterId);
        return ResponseEntity.ok().body(new ChapterDTO(chapter));
    }

    @PatchMapping(value = "/{chapterId}")
    @Operation(summary = "Update chapter", description = "Update a chapter specified by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chapter updated successfully"),
        @ApiResponse(responseCode = "404", description = "Chapter not found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> updateChapter(@RequestBody @Valid ChapterUpdateDTO chapterUpdateDTO, @PathVariable String chapterId, Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());
        chapterService.updateChapter(chapterUpdateDTO, chapterId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{chapterId}")
    @Operation(summary = "Delete a chapter", description = "Deletes a chapter and removes its reference in the associated Novel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Chapter deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Chapter not found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteChapter(@PathVariable String chapterId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        chapterService.deleteChapter(chapterId, user.getId());

        return ResponseEntity.noContent().build();
    }
}
