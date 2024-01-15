package com.milansomyk.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookQLDto {
    private int id;
    private String title;
    private String author;
    private int price;
    private int publicationYear;
}
