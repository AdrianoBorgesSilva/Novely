package com.novely.novely.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.novely.novely.domain.Novel;

@Repository
public interface NovelRepository extends MongoRepository<Novel, String>{
    
}
