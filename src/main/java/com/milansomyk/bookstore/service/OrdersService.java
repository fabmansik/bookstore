package com.milansomyk.bookstore.service;

import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.entity.Book;
import com.milansomyk.bookstore.entity.Orders;
import com.milansomyk.bookstore.entity.User;
import com.milansomyk.bookstore.mapper.OrdersMapper;
import com.milansomyk.bookstore.repository.BookRepository;
import com.milansomyk.bookstore.repository.OrdersRepository;
import com.milansomyk.bookstore.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Data
@Slf4j
public class OrdersService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrdersRepository ordersRepository;
    private final OrdersMapper ordersMapper;
    public ResponseContainer createOrder(List<Integer> bookIds, String username){
        ResponseContainer responseContainer = new ResponseContainer();
        if(CollectionUtils.isEmpty(bookIds)){
            log.error("no books provided");
            return responseContainer.setErrorMessageAndStatusCode("no books provided", HttpStatus.BAD_REQUEST.value());
        }
        if(!StringUtils.hasText(username)){
            log.error("user not found");
            return responseContainer.setErrorMessageAndStatusCode("user not found", HttpStatus.BAD_REQUEST.value());
        }
        User found;
        try {
            found = userRepository.findByUsername(username).orElse(null);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(found)){
            log.error("user not found");
            return responseContainer.setErrorMessageAndStatusCode("user not found", HttpStatus.BAD_REQUEST.value());
        }
        Orders order = new Orders();
        order.setOrderDate(new Date(System.currentTimeMillis()));
        order.setUser(found);
        List<Book> bookList = new ArrayList<>();
        for (Integer integer : bookIds) {
            Book book;
            try{
                book = bookRepository.findById(integer).orElse(null);
            }catch (Exception e){
                log.error(e.getMessage());
                return responseContainer.setErrorMessageAndStatusCode(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            if(ObjectUtils.isEmpty(book)){
                log.error("book not found");
                return responseContainer.setErrorMessageAndStatusCode("book not found",HttpStatus.BAD_REQUEST.value());
            }
            bookList.add(book);
        }
        order.setBooks(bookList);
        int summary = 0;
        List<Integer> list = bookList.stream().map(Book::getPrice).toList();
        for (Integer integer : list) summary = summary + integer;
        order.setOrderPrice(summary);
        Orders saved;
        try{
            saved = ordersRepository.save(order);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseContainer.setSuccessResult(ordersMapper.toDto(saved));
    }
}
