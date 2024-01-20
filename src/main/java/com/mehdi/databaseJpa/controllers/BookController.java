package com.mehdi.databaseJpa.controllers;

import com.mehdi.databaseJpa.domain.dto.BookDto;
import com.mehdi.databaseJpa.domain.entities.BookEntity;
import com.mehdi.databaseJpa.mappers.Mapper;
import com.mehdi.databaseJpa.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {
    private final BookService bookService;
    private final Mapper<BookEntity, BookDto> bookMapper;
    public BookController(BookService bookService, Mapper<BookEntity, BookDto> bookMapper){
        this.bookService=bookService;
        this.bookMapper = bookMapper;
    }
    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto book)
    {
        boolean bookExists = bookService.isExists(isbn);
        BookEntity bookEntity=bookMapper.mapFrom(book);
        BookEntity savedBookEntity = bookService.createUpdateBook(isbn, bookEntity);
        if(bookExists){
            //update
            return new ResponseEntity<>(
                    bookMapper.mapTo(savedBookEntity),
                    HttpStatus.OK);
        }else{

            return new ResponseEntity<>(
                    bookMapper.mapTo(savedBookEntity),
                    HttpStatus.CREATED);
        }

    }
    @GetMapping(path = "/books")
    public Page<BookDto> listBooks(Pageable pageable){
        Page<BookEntity> books = bookService.findAll(pageable);
        return books.map(bookMapper::mapTo);
        /*return books.stream().
                map(bookMapper::mapTo).
                collect(Collectors.toList());*/
    }
    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> findOne(@PathVariable("isbn") String isbn){
        Optional<BookEntity> book = bookService.findOne(isbn);
        return book.map(bookEntity -> {
            BookDto bookDto = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdate(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto){
        if(!bookService.isExists(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BookEntity bookEntity = bookService.
                partialUpdate(isbn, bookMapper.mapFrom(bookDto));

        BookDto SavedbookDto = bookMapper.mapTo(bookEntity);
        return new ResponseEntity<>(SavedbookDto, HttpStatus.OK);
    }
    @DeleteMapping(path = "books/{isbn}")
    public ResponseEntity delete(@PathVariable("isbn") String isbn){
        bookService.delete(isbn);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
