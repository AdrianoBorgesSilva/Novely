package com.novely.novely.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.novely.novely.domain.Chapter;

@Repository
public interface ChapterRepository extends MongoRepository<Chapter, String>{
    List<Chapter> findByNovelId(String novelId);
}
