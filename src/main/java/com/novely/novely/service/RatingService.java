package com.novely.novely.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.novely.novely.domain.Novel;
import com.novely.novely.domain.Rating;
import com.novely.novely.dto.RatingCreateDTO;
import com.novely.novely.exception.ObjectNotFoundException;
import com.novely.novely.exception.UnauthorizedActionException;
import com.novely.novely.repository.RatingRepository;

@Service
public class RatingService {
    
    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final NovelService novelService;

    public RatingService(RatingRepository ratingRepository, UserService userService, NovelService novelService) {
        this.ratingRepository = ratingRepository;
        this.userService = userService;
        this.novelService = novelService;
    }

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public Rating findById(String id) {
        return ratingRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Rating not found."));
    }

    public List<Rating> findByNovelId(String novelId){

        novelService.findById(novelId);

        return ratingRepository.findByNovelId(novelId);
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

    public void deleteAllByNovelId(String novelId) {
        List<Rating> list = ratingRepository.findByNovelId(novelId);
        ratingRepository.deleteAll(list);
    }
}
