package com.milansomyk.bookstore.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewsAndCommentsDto {
    private int bookId;
    private List<CommentDto> comments;
    private List<ReviewDto> reviews;
}
