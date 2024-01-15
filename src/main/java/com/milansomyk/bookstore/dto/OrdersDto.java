package com.milansomyk.bookstore.dto;

import com.milansomyk.bookstore.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto {
    private int orderId;
    private UserDto user;
    private List<Book> books;
    private Date orderDate;
    private int orderPrice;
}
