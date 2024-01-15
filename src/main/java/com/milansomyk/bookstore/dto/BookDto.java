package com.milansomyk.bookstore.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDto {
    private int id;
    @NotBlank(message = "title required")
    @Size(min = 3, max = 20, message = "title: min: {min}, max: {max} characters")
    private String title;
    @NotBlank(message = "author required")
    @Size(min = 3, max = 20, message = "author: min: {min}, max: {max} characters")
    private String author;
    @Positive
    @DecimalMin(value = "1")
    private int price;
    @PastOrPresent
    private Year publicationYear;
}
