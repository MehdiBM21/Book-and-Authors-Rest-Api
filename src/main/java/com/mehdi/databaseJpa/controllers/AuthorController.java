package com.mehdi.databaseJpa.controllers;

import com.mehdi.databaseJpa.domain.dto.AuthorDto;
import com.mehdi.databaseJpa.domain.entities.AuthorEntity;
import com.mehdi.databaseJpa.mappers.Mapper;
import com.mehdi.databaseJpa.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {
    private AuthorService authorService;
    private Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper){
        this.authorService = authorService;
        this.authorMapper=authorMapper;
    }
    @PostMapping(path = "/authors")
    public AuthorDto createAuthor(@RequestBody AuthorDto authorDto){
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);
        return authorMapper.mapTo(savedAuthorEntity);
    }
    @GetMapping(path = "/authors")
    public List<AuthorDto> listAuthors(){
        List<AuthorEntity> authors = authorService.findAll();
        return authors.stream().
                map(authorMapper::mapTo).
                collect(Collectors.toList());
    }
    @GetMapping(path= "authors/{id}")
    public ResponseEntity<AuthorDto>findOne(@PathVariable("id") Long id){
        Optional<AuthorEntity> author = authorService.findOne(id);
        return author.map(
                authorEntity -> {
                    AuthorDto authorDto =authorMapper.mapTo(authorEntity);
                    return new ResponseEntity<>(authorDto, HttpStatus.OK);
                }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping(path = "authors/{id}")
    public ResponseEntity<AuthorDto>fullUpdateAuthor(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto author){
        if(!authorService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AuthorEntity authorEntity = authorService.save(
                authorMapper.mapFrom(author));
        return new ResponseEntity<>(
                authorMapper.mapTo(authorEntity),
                HttpStatus.OK);
    }
    @PatchMapping(path = "authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdate(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto){
        if(!authorService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity author = authorService.partialUpdate(id, authorEntity);
        AuthorDto savedAutorDto = authorMapper.mapTo(author);
        return new ResponseEntity<>(savedAutorDto, HttpStatus.OK);
    }
    @DeleteMapping(path = "authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable("id") Long id){
        authorService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
