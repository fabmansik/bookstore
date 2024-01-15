package com.milansomyk.bookstore.document;

import com.milansomyk.bookstore.dto.CommentDto;
import com.milansomyk.bookstore.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "reviews_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsAndComments {
    @Id
    private int bookId;
    private List<CommentDto> comments;
    private List<ReviewDto> reviews;

    public void addReview(ReviewDto reviewDto){
        this.reviews.add(reviewDto);
    }
    public void addComment(CommentDto commentDto){
        this.comments.add(commentDto);
    }
}
