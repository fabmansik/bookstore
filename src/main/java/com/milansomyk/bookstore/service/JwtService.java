package com.milansomyk.bookstore.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;

public interface JwtService {
    boolean isTokenExpired(String token);
    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    Duration extractDuration(String token);
    boolean isRefreshType(String token);
    String extractTokenFromAuth(String auth);
}
