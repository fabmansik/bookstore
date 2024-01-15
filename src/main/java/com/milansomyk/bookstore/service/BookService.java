package com.milansomyk.bookstore.service;

import com.milansomyk.bookstore.dto.BookDto;
import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.entity.Book;
import com.milansomyk.bookstore.mapper.BookMapper;
import com.milansomyk.bookstore.repository.BookRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Data
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final ReviewsAndCommentsService reviewsAndCommentsService;

    public ResponseContainer create(BookDto bookDto) {
        ResponseContainer responseContainer = new ResponseContainer();
        if (ObjectUtils.isEmpty(bookDto)) {
            log.error("book is empty");
            return responseContainer.setErrorMessageAndStatusCode("book is empty", HttpStatus.BAD_REQUEST.value());
        }
        Book saved;
        try {
            saved = bookRepository.save(bookMapper.fromDto(bookDto));
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        ResponseContainer mongoResponse = reviewsAndCommentsService.initCreate(saved.getId());
        if (mongoResponse.isError()){
            log.error(mongoResponse.getErrorMessage());
            return mongoResponse;
        }
        return responseContainer.setCreatedResult(bookMapper.toDto(saved));
    }
}
