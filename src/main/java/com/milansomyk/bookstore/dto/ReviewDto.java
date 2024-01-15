package com.milansomyk.bookstore.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewDto {
    private int userId;
    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private int rating;
    private String reviewText;
    private Date timestamp;
}
