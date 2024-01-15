package com.milansomyk.bookstore.controller;

import com.milansomyk.bookstore.dto.BookDto;
import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.service.AuthService;
import com.milansomyk.bookstore.service.BookService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Data
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final AuthService authService;
    @PostMapping
    public ResponseEntity<ResponseContainer> create(@RequestBody @Valid BookDto bookDto){
        ResponseContainer responseContainer = bookService.create(bookDto);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }
    @GetMapping
    public ResponseEntity<ResponseContainer> getAll(){
        ResponseContainer responseContainer = bookService.getAll();
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseContainer> findById(@PathVariable int id, @RequestHeader("AUTHORIZATION") String token){
        String username = authService.extractUsernameFromAuth(token);
        ResponseContainer responseContainer = bookService.findById(id, username);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseContainer> updateById(@PathVariable int id, @RequestBody @Valid BookDto bookDto){
        ResponseContainer responseContainer = bookService.updateById(id, bookDto);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseContainer> deleteById(@PathVariable int id){
        ResponseContainer responseContainer = bookService.deleteById(id);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }
}
