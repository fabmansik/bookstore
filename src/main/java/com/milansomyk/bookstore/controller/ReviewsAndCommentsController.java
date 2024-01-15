package com.milansomyk.bookstore.controller;

import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.service.AuthService;
import com.milansomyk.bookstore.service.ReviewsAndCommentsService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
public class ReviewsAndCommentsController {
    private final ReviewsAndCommentsService reviewsAndCommentsService;
    private final AuthService authService;
    @PostMapping("/comments")
    public ResponseEntity<ResponseContainer> addComment(@RequestHeader("AUTHORIZATION") String token,
                                                        @RequestBody String text, @RequestParam int bookId){
        String username = authService.extractUsernameFromAuth(token);
        ResponseContainer responseContainer = reviewsAndCommentsService.addComment(bookId, username, text);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }
    @PostMapping("/reviews")
    public ResponseEntity<ResponseContainer> addReview(@RequestHeader("AUTHORIZATION") String token,
                                                        @RequestBody String text, @RequestParam int bookId, @RequestParam int rating){
        String username = authService.extractUsernameFromAuth(token);
        ResponseContainer responseContainer = reviewsAndCommentsService.addReview(bookId, username, text, rating);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }
}
