package com.milansomyk.bookstore.controller;

import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.dto.requests.RefreshRequest;
import com.milansomyk.bookstore.dto.requests.SignInRequest;
import com.milansomyk.bookstore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<ResponseContainer> signIn(@RequestBody SignInRequest signInRequest){
        ResponseContainer responseContainer = authService.login(signInRequest);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseContainer> refresh(@RequestBody RefreshRequest refreshRequest){
        ResponseContainer responseContainer = authService.refresh(refreshRequest);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }
}
