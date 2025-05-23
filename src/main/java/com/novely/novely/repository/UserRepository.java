package com.novely.novely.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.novely.novely.domain.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
