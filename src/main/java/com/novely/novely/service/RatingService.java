package com.novely.novely.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novely.novely.domain.Novel;
import com.novely.novely.domain.Rating;
import com.novely.novely.dto.RatingCreateDTO;
import com.novely.novely.exception.ObjectNotFoundException;
import com.novely.novely.exception.UnauthorizedActionException;
import com.novely.novely.repository.RatingRepository;

@Service
public class RatingService {
    
    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    UserService userService;

    @Autowired
    NovelService novelService;

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public Rating findById(String id) {
        return ratingRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Rating not found."));
    }

    public Rating createRating(RatingCreateDTO ratingCreateDTO, String novelId, String userId) {

        Novel novel = novelService.findById(novelId);
        Rating rating = new Rating(null, ratingCreateDTO.getRate(), userId, novelId, LocalDateTime.now());

        rating = ratingRepository.save(rating);
        userService.addRatingToUser(userId, rating.getId());
        novelService.addRatingToNovel(novel.getId(), rating.getId());

        return rating;
    }

    public void deleteRating(String ratingId, String userId) {
        
        Rating rating = findById(ratingId);

        if (!rating.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("You can not delete this rating.");
        }

        userService.removeRatingFromUser(userId, ratingId);
        novelService.removeRatingFromNovel(rating.getNovelId(), ratingId);
        
        ratingRepository.deleteById(ratingId);
    }
}
