package com.milansomyk.bookstore.mapper;

import com.milansomyk.bookstore.dto.BookDto;
import com.milansomyk.bookstore.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public BookDto toDto(Book book){
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .publicationYear(book.getPublicationYear())
                .build();
    }
    public Book fromDto (BookDto bookDto){
        return new Book(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthor(), bookDto.getPrice(), bookDto.getPublicationYear());
    }
}
