package com.milansomyk.bookstore.mapper;

import com.milansomyk.bookstore.dto.UserDto;
import com.milansomyk.bookstore.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toResponseDto(User user){
        return UserDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
    public User fromDto(UserDto userDto){
        return new User(userDto.getUserId(), userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), userDto.getPhone());
    }
}
