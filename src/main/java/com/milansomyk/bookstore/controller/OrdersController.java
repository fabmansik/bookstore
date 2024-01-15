package com.milansomyk.bookstore.controller;

import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.service.AuthService;
import com.milansomyk.bookstore.service.JwtService;
import com.milansomyk.bookstore.service.OrdersService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Data
@RequestMapping("/order")
public class OrdersController {
    private final OrdersService ordersService;
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ResponseContainer> createOrder(@RequestBody @Valid List<Integer> bookIds, @RequestHeader("Authorization") String auth){
        String username = authService.extractUsernameFromAuth(auth);
        ResponseContainer responseContainer = ordersService.createOrder(bookIds, username);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }

}
