package com.devsuperior.workshopmongo.services;

import com.devsuperior.workshopmongo.domain.Post;
import com.devsuperior.workshopmongo.domain.User;
import com.devsuperior.workshopmongo.dto.UserDTO;
import com.devsuperior.workshopmongo.repository.PostRepository;
import com.devsuperior.workshopmongo.repository.UserRepository;
import com.devsuperior.workshopmongo.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository repo;

    public Post findById(String id) {
        Optional<Post> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }
}
