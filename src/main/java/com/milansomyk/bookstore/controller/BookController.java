package com.milansomyk.bookstore.controller;

import com.milansomyk.bookstore.dto.BookDto;
import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.service.BookService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Data
@RequestMapping(value = "/management")
public class BookController {
    private final BookService bookService;
    @PostMapping(value = "/book")
    public ResponseEntity<ResponseContainer> create(@RequestBody @Valid BookDto bookDto){
        ResponseContainer responseContainer = bookService.create(bookDto);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }
}
