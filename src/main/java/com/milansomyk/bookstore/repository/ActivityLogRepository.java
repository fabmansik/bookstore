package com.milansomyk.bookstore.repository;

import com.milansomyk.bookstore.document.ActivityLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityLogRepository extends MongoRepository<ActivityLog, Integer> {
}
