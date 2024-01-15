package com.milansomyk.bookstore.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LogDto {
    private Date timestamp;
    private String activityType;
    private String Details;
}
