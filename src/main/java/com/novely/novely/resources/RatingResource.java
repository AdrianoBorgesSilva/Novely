package com.novely.novely.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.novely.novely.service.RatingService;
import com.novely.novely.service.UserService;


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

    @DeleteMapping(value = "/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable String ratingId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName()); 
        ratingService.deleteRating(ratingId, user.getId());
        
        return ResponseEntity.noContent().build();
    }
}
