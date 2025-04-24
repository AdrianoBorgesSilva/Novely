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

import com.novely.novely.domain.Rating;
import com.novely.novely.domain.User;
import com.novely.novely.dto.RatingCreateDTO;
import com.novely.novely.dto.RatingDTO;
import com.novely.novely.service.RatingService;
import com.novely.novely.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/ratings")
public class RatingResource {
    
    @Autowired
    RatingService ratingService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<RatingDTO>> findAll() {
        List<RatingDTO> list = ratingService.findAll().stream().map(RatingDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{ratingId}")
    public ResponseEntity<RatingDTO> findById(@PathVariable String ratingId) {
        Rating rating = ratingService.findById(ratingId);
        return ResponseEntity.ok().body(new RatingDTO(rating));
    }   
    
    @PostMapping(value = "/novels/{novelId}")
    public ResponseEntity<Void> createRating(@RequestBody @Valid RatingCreateDTO ratingCreateDTO, @PathVariable String novelId, Authentication authentication){

        User user = userService.findByEmail(authentication.getName());
        Rating rating = ratingService.createRating(ratingCreateDTO, novelId, user.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(rating.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable String ratingId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName()); 
        ratingService.deleteRating(ratingId, user.getId());
        
        return ResponseEntity.noContent().build();
    }
}
