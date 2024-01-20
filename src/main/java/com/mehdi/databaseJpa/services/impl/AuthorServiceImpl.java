package com.mehdi.databaseJpa.services.impl;

import com.mehdi.databaseJpa.domain.dto.AuthorDto;
import com.mehdi.databaseJpa.domain.entities.AuthorEntity;
import com.mehdi.databaseJpa.repositories.AuthorRepository;
import com.mehdi.databaseJpa.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    @Override
    public AuthorEntity save(AuthorEntity author) {
        AuthorEntity authorEntity = authorRepository.save(author);
        System.out.println(authorEntity);
        return authorEntity;

    }

    @Override
    public List<AuthorEntity> findAll() {
        return StreamSupport.stream(
                authorRepository.findAll().spliterator(),
                false).collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorEntity> findOne(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity) {
        authorEntity.setId(id);
        return authorRepository.findById(id).map(
                existingAuthor -> {
                    Optional.ofNullable(authorEntity.getName()).
                            ifPresent(existingAuthor::setName);
                    Optional.ofNullable(authorEntity.getAge()).
                            ifPresent(existingAuthor::setAge);
                    return authorRepository.save(existingAuthor);
                }).orElseThrow(() -> new RuntimeException("Author not found"));
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
