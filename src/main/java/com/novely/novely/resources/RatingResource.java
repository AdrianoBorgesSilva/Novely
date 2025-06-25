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
@Tag(name = "Ratings", description = "Controller responsible for actions related to ratings")
public class RatingResource {
    
    private final RatingService ratingService;
    private final UserService userService;
    
    public RatingResource(RatingService ratingService, UserService userService) {
        this.ratingService = ratingService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "List all reviews", description = "Returns all ratings registered in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reviews returned successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<RatingDTO>> findAll() {
        List<RatingDTO> list = ratingService.findAll().stream().map(RatingDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{ratingId}")
    @Operation(summary = "Return a single rating", description = "Returns the rating data specified by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rating returned successfully"),
        @ApiResponse(responseCode = "404", description = "Rating not found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RatingDTO> findById(@PathVariable String ratingId) {
        Rating rating = ratingService.findById(ratingId);
        return ResponseEntity.ok().body(new RatingDTO(rating));
    }   

    @DeleteMapping(value = "/{ratingId}")
    @Operation(summary = "Delete a rating", description = "Deletes a rating and removes its reference in the associated Novel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Rating deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Rating not found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteRating(@PathVariable String ratingId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName()); 
        ratingService.deleteRating(ratingId, user.getId());
        
        return ResponseEntity.noContent().build();
    }
}
