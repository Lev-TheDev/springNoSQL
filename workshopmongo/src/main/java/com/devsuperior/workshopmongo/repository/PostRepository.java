package com.devsuperior.workshopmongo.repository;

import com.devsuperior.workshopmongo.domain.Post;
import com.devsuperior.workshopmongo.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
}
