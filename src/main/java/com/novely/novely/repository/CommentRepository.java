package com.novely.novely.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.novely.novely.domain.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String>{
    List<Comment> findByNovelId(String novelId);
}
