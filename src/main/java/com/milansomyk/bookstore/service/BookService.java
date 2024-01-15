package com.milansomyk.bookstore.service;

import com.milansomyk.bookstore.controller.BookQLController;
import com.milansomyk.bookstore.dto.BookDto;
import com.milansomyk.bookstore.dto.BookQLDto;
import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.entity.Book;
import com.milansomyk.bookstore.entity.User;
import com.milansomyk.bookstore.enums.LogType;
import com.milansomyk.bookstore.mapper.BookMapper;
import com.milansomyk.bookstore.repository.BookRepository;
import com.milansomyk.bookstore.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final ReviewsAndCommentsService reviewsAndCommentsService;
    private final ActivityLogService activityLogService;
    private final UserRepository userRepository;

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
    public ResponseContainer getAll(){
        ResponseContainer responseContainer = new ResponseContainer();
        List<Book> bookList;
        try{
            bookList = bookRepository.findAll();
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        List<BookDto> list = bookList.stream().map(bookMapper::toDto).toList();
        return responseContainer.setSuccessResult(list);
    }
    public ResponseContainer findById(int id, String username){
        ResponseContainer responseContainer = new ResponseContainer();
        Book found;
        if(ObjectUtils.isEmpty(id)){
            log.error("id is null");
            return responseContainer.setErrorMessageAndStatusCode("id is null", HttpStatus.BAD_REQUEST.value());
        }
        try{
            found = bookRepository.findById(id).orElse(null);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(found)){
            log.error("book not found");
            return responseContainer.setErrorMessageAndStatusCode("book not found",HttpStatus.BAD_REQUEST.value());
        }
        if(StringUtils.hasText(username)){
            User user;
            try{
                user = userRepository.findByUsername(username).orElse(null);
            }catch (Exception e){
                log.error(e.getMessage());
                return responseContainer.setErrorMessageAndStatusCode(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            if(user==null){
                log.error("user not found");
                return responseContainer.setErrorMessageAndStatusCode("user not found", HttpStatus.BAD_REQUEST.value());
            }
            ResponseContainer logged = activityLogService.log(user.getUserId(), id, LogType.BOOK_VIEW);
            if(logged.isError()){
                log.error(logged.getErrorMessage());
                return logged;
            }
        }
        return responseContainer.setSuccessResult(bookMapper.toDto(found));
    }
    public ResponseContainer updateById(int id, BookDto bookDto){
        ResponseContainer responseContainer = new ResponseContainer();
        if(ObjectUtils.isEmpty(id)){
            log.error("id is null");
            return responseContainer.setErrorMessageAndStatusCode("id is null", HttpStatus.BAD_REQUEST.value());
        }
        if(ObjectUtils.isEmpty(bookDto)){
            log.error("book is null");
            return responseContainer.setErrorMessageAndStatusCode("book is null", HttpStatus.BAD_REQUEST.value());
        }
        Book found;
        try{
            found = bookRepository.findById(id).orElse(null);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(found)){
            log.error("book not found");
            return responseContainer.setErrorMessageAndStatusCode("book not found", HttpStatus.BAD_REQUEST.value());
        }
        found.setTitle(bookDto.getTitle());
        found.setAuthor(bookDto.getAuthor());
        found.setPrice(bookDto.getPrice());
        found.setPublicationYear(bookDto.getPublicationYear());
        Book saved;
        try{
            saved = bookRepository.save(found);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
        return responseContainer.setSuccessResult(bookMapper.toDto(saved));
    }
    public ResponseContainer deleteById(int id){
        ResponseContainer responseContainer = new ResponseContainer();
        if(ObjectUtils.isEmpty(id)){
            log.error("id is null");
            return responseContainer.setErrorMessageAndStatusCode("id is null",HttpStatus.BAD_REQUEST.value());
        }
        try{
            bookRepository.deleteById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseContainer.setSuccessResult("Book with id: "+id+" deleted");
    }
    public List<BookQLDto> getAllBooksQL(){
        List<Book> bookList = new ArrayList<>();
        try {
            bookList = bookRepository.findAll();
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return bookList.stream().map(book -> new BookQLDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getPublicationYear().getValue())).toList();
    }
    public BookQLDto findByIdQL(int id){
        Book book;
        try {
            book = bookRepository.findById(id).orElse(null);
        } catch (Exception e){
            log.error(e.getMessage());
            return new BookQLDto();
        }
        if(ObjectUtils.isEmpty(book)){
            log.error("book not found");
            return new BookQLDto();
        }
        return new BookQLDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getPublicationYear().getValue());
    }
    public BookQLDto addBookQL(BookQLController.BookInput book){
        if (ObjectUtils.isEmpty(book)) {
            log.error("book is empty");
            return new BookQLDto();
        }
        Book createdBook = new Book(book.title(), book.author(), book.price(), Year.of(book.publicationYear()));
        Book saved;
        try {
            saved = bookRepository.save(createdBook);
        } catch (Exception e){
            log.error(e.getMessage());
            return new BookQLDto();
        }
        ResponseContainer mongoResponse = reviewsAndCommentsService.initCreate(saved.getId());
        if (mongoResponse.isError()){
            log.error(mongoResponse.getErrorMessage());
            return new BookQLDto();
        }
        return new BookQLDto(saved.getId(),saved.getTitle(),saved.getAuthor(),saved.getPrice(),saved.getPublicationYear().getValue());
    }
    public int deleteByIdQL(int id){
        if (ObjectUtils.isEmpty(id)){
            log.error("id is null");
            return 0;
        }
        try{
            bookRepository.deleteById(id);
        } catch (Exception e){
            log.error("book not found");
            return 0;
        }
        return id;
    }
    public BookQLDto updateByIdQL(BookQLController.BookUpdate bookUpdate){
        int id = bookUpdate.id();
        if(ObjectUtils.isEmpty(id)){
            log.error("book not found");
            return null;
        }
        Book found;
        try {
            found = bookRepository.findById(id).orElse(null);
        } catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
        if(ObjectUtils.isEmpty(found)){
            log.error("book not found");
            return null;
        }
        if(StringUtils.hasText(bookUpdate.title())){
            found.setTitle(bookUpdate.title());
        }
        if(StringUtils.hasText(bookUpdate.author())){
            found.setAuthor(bookUpdate.author());
        }
        if(bookUpdate.price()!=0){
            found.setPrice(bookUpdate.price());
        }
        if(bookUpdate.publicationYear()!=0){
            found.setPublicationYear(Year.of(bookUpdate.publicationYear()));
        }
        Book saved;
        try{
            saved = bookRepository.save(found);
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
        return new BookQLDto(saved.getId(), saved.getTitle(),saved.getAuthor(),saved.getPrice(),saved.getPublicationYear().getValue());
    }
}
