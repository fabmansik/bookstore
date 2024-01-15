package com.milansomyk.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
@Data
public class LogDetails {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int bookId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> booksIds;
}
