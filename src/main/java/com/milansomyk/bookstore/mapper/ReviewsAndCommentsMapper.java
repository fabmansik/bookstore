package com.milansomyk.bookstore.mapper;

import com.milansomyk.bookstore.document.ReviewsAndComments;
import com.milansomyk.bookstore.dto.ReviewsAndCommentsDto;
import org.springframework.stereotype.Component;

@Component
public class ReviewsAndCommentsMapper {
    public ReviewsAndCommentsDto toDto(ReviewsAndComments reviewsAndComments){
        return ReviewsAndCommentsDto.builder()
                .bookId(reviewsAndComments.getBookId())
                .comments(reviewsAndComments.getComments())
                .reviews(reviewsAndComments.getReviews())
                .build();
    }
    public ReviewsAndComments fromDto(ReviewsAndCommentsDto reviewsAndCommentsDto){
        return new ReviewsAndComments(reviewsAndCommentsDto.getBookId(), reviewsAndCommentsDto.getComments(), reviewsAndCommentsDto.getReviews());
    }
}
