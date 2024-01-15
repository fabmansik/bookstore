package com.milansomyk.bookstore.controller;

import com.milansomyk.bookstore.dto.BookQLDto;
import com.milansomyk.bookstore.repository.BookRepository;
import com.milansomyk.bookstore.service.BookService;
import lombok.Data;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Data
public class BookQLController {
    private final BookRepository bookRepository;
    private final BookService bookService;
    @QueryMapping
    public List<BookQLDto> books(){
        return bookService.getAllBooksQL();
    }
    @QueryMapping
    public BookQLDto bookById(@Argument int id){return bookService.findByIdQL(id);}
    @MutationMapping
    public BookQLDto addBook(@Argument BookInput book){return bookService.addBookQL(book);}
    @MutationMapping
    public int deleteBook(@Argument int id){return bookService.deleteByIdQL(id);}
    @MutationMapping
    public BookQLDto updateBook(@Argument BookUpdate book){return bookService.updateByIdQL(book);}
    public record BookInput(String title, String author, int price, int publicationYear) {}
    public record BookUpdate(int id, String title, String author, int price, int publicationYear) {}
}
