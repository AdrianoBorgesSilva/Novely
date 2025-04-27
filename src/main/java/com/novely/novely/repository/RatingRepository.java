package com.novely.novely.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.novely.novely.domain.Rating;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String>{
    List<Rating> findByNovelId(String novelId);
}
