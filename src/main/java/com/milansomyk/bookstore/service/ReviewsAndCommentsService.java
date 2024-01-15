package com.milansomyk.bookstore.service;

import com.milansomyk.bookstore.document.ReviewsAndComments;
import com.milansomyk.bookstore.dto.CommentDto;
import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.dto.ReviewDto;
import com.milansomyk.bookstore.entity.User;
import com.milansomyk.bookstore.mapper.ReviewsAndCommentsMapper;
import com.milansomyk.bookstore.repository.ReviewsAndCommentsRepository;
import com.milansomyk.bookstore.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;

@Service
@Data
@Slf4j
public class ReviewsAndCommentsService {
    private final ReviewsAndCommentsRepository reviewsAndCommentsRepository;
    private final ReviewsAndCommentsMapper reviewsAndCommentsMapper;
    private final UserRepository userRepository;
    public ResponseContainer initCreate(int bookId){
        ReviewsAndComments reviewsAndComments = new ReviewsAndComments();
        reviewsAndComments.setBookId(bookId);
        reviewsAndComments.setComments(new ArrayList<>());
        reviewsAndComments.setReviews(new ArrayList<>());
        ResponseContainer responseContainer = new ResponseContainer();
        ReviewsAndComments saved;
        try {
            saved = reviewsAndCommentsRepository.save(reviewsAndComments);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseContainer.setSuccessResult(reviewsAndCommentsMapper.toDto(saved));
    }
    public ResponseContainer addComment(int bookId, String username, String text){
        ResponseContainer responseContainer = new ResponseContainer();
        if(ObjectUtils.isEmpty(bookId)){
            log.error("book id is empty");
            return responseContainer.setErrorMessageAndStatusCode("book is empty", HttpStatus.BAD_REQUEST.value());
        }
        if(username.isEmpty()){
            log.error("username is empty");
            return responseContainer.setErrorMessageAndStatusCode("username is empty",HttpStatus.BAD_REQUEST.value());
        }
        if(text.isEmpty()){
            log.error("text is empty");
            return responseContainer.setErrorMessageAndStatusCode("text is empty",HttpStatus.BAD_REQUEST.value());
        }
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText(text);
        commentDto.setTimestamp(new Date(System.currentTimeMillis()));
        User found;
        try {
            found = userRepository.findByUsername(username).orElse(null);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(found)){
            log.error("user not found");
            return responseContainer.setErrorMessageAndStatusCode("user not found", HttpStatus.BAD_REQUEST.value());
        }
        commentDto.setUserId(found.getUserId());
        ReviewsAndComments reviewsAndComments;
        try{
            reviewsAndComments = reviewsAndCommentsRepository.findById(bookId).orElse(null);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(reviewsAndComments)){
            log.error("book not found");
            return responseContainer.setErrorMessageAndStatusCode("book not found", HttpStatus.BAD_REQUEST.value());
        }
        reviewsAndComments.addComment(commentDto);
        ReviewsAndComments saved;
        try{
            saved = reviewsAndCommentsRepository.save(reviewsAndComments);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseContainer.setSuccessResult(reviewsAndCommentsMapper.toDto(saved));
    }
    public ResponseContainer addReview(int bookId, String username, String text, int rating){
        ResponseContainer responseContainer = new ResponseContainer();
        if(ObjectUtils.isEmpty(bookId)){
            log.error("book id is empty");
            return responseContainer.setErrorMessageAndStatusCode("book is empty", HttpStatus.BAD_REQUEST.value());
        }
        if(username.isEmpty()){
            log.error("username is empty");
            return responseContainer.setErrorMessageAndStatusCode("username is empty",HttpStatus.BAD_REQUEST.value());
        }
        if(ObjectUtils.isEmpty(rating)){
            log.error("rating is empty");
            return responseContainer.setErrorMessageAndStatusCode("rating is empty", HttpStatus.BAD_REQUEST.value());
        }
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewText(text);
        reviewDto.setTimestamp(new Date(System.currentTimeMillis()));
        User found;
        try {
            found = userRepository.findByUsername(username).orElse(null);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(found)){
            log.error("user not found");
            return responseContainer.setErrorMessageAndStatusCode("user not found", HttpStatus.BAD_REQUEST.value());
        }
        reviewDto.setUserId(found.getUserId());
        if(rating>10||rating<1){
            log.error("rating should be between 1 to 10");
            return responseContainer.setErrorMessageAndStatusCode("rating should be between 1 to 10", HttpStatus.BAD_REQUEST.value());
        }
        reviewDto.setRating(rating);
        ReviewsAndComments reviewsAndComments;
        try{
            reviewsAndComments = reviewsAndCommentsRepository.findById(bookId).orElse(null);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(reviewsAndComments)){
            log.error("book not found");
            return responseContainer.setErrorMessageAndStatusCode("book not found", HttpStatus.BAD_REQUEST.value());
        }
        reviewsAndComments.addReview(reviewDto);
        ReviewsAndComments saved;
        try{
            saved = reviewsAndCommentsRepository.save(reviewsAndComments);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseContainer.setSuccessResult(reviewsAndCommentsMapper.toDto(saved));
    }
}
