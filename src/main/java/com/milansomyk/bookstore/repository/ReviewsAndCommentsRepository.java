package com.milansomyk.bookstore.repository;

import com.milansomyk.bookstore.document.ReviewsAndComments;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewsAndCommentsRepository extends MongoRepository<ReviewsAndComments, Integer> {
}
