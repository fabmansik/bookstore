package com.milansomyk.bookstore.controller;

import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.dto.UserDto;
import com.milansomyk.bookstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController{
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseContainer> create(@RequestBody @Valid UserDto userDto){
        ResponseContainer responseContainer = userService.register(userDto);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }

}
