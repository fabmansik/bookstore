package com.milansomyk.bookstore.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {
    private int userId;
    private String commentText;
    private Date timestamp;
}
