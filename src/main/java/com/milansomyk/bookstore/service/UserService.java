package com.milansomyk.bookstore.service;

import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.dto.UserDto;
import com.milansomyk.bookstore.entity.User;
import com.milansomyk.bookstore.mapper.UserMapper;
import com.milansomyk.bookstore.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Data
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ActivityLogService activityLogService;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public ResponseContainer register(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        ResponseContainer responseContainer = new ResponseContainer();
        if (ObjectUtils.isEmpty(user)) {
            log.error("user is null");
            return responseContainer.setErrorMessageAndStatusCode("user is null", HttpStatus.BAD_REQUEST.value());
        }
        if (!StringUtils.hasText(user.getUsername()) && !StringUtils.hasText(user.getPassword())
                && !StringUtils.hasText(user.getEmail()) && ObjectUtils.isEmpty(user.getPhone())) {
            log.error("not enough user credentials");
            return responseContainer.setErrorMessageAndStatusCode("not enough user credentials", HttpStatus.BAD_REQUEST.value());
        }
        responseContainer = isUsernameAlreadyExists(user.getUsername(),responseContainer);
        if(responseContainer.isError()){
            log.error(responseContainer.getErrorMessage());
            return responseContainer;
        }

        responseContainer = isEmailAlreadyUsed(user.getEmail(), responseContainer);
        if (responseContainer.isError()){
            log.error(responseContainer.getErrorMessage());
            return responseContainer;
        }

        responseContainer = isPhoneAlreadyUsed(user.getPhone(), responseContainer);
        if (responseContainer.isError()){
            log.error(responseContainer.getErrorMessage());
            return responseContainer;
        }
        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
        user.setRole("USER");
        User saved;
        try {
            saved = userRepository.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        UserDto responseDto = userMapper.toResponseDto(saved);
        ResponseContainer initResponse = activityLogService.init(saved.getUserId());
        if(initResponse.isError()){
            log.error(initResponse.getErrorMessage());
            return responseContainer.setErrorMessageAndStatusCode(initResponse.getErrorMessage(), initResponse.getStatusCode());
        }
        return responseContainer.setSuccessResult(responseDto);
    }

    public ResponseContainer isUsernameAlreadyExists(String username, ResponseContainer responseContainer) {
        User user;
        try {
            user = userRepository.findByUsername(username).orElse(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
        if(!ObjectUtils.isEmpty(user)){
            log.error("user already exists");
            return responseContainer.setErrorMessageAndStatusCode("user already exists",HttpStatus.BAD_REQUEST.value());
        }
        return responseContainer;
    }
    public ResponseContainer isEmailAlreadyUsed(String email, ResponseContainer responseContainer){
        User user;
        try{
            user = userRepository.findByEmail(email).orElse(null);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
        if(!ObjectUtils.isEmpty(user)){
            log.error("user already exists");
            return responseContainer.setErrorMessageAndStatusCode("user already exists",HttpStatus.BAD_REQUEST.value());
        }
        return responseContainer;
    }
    public ResponseContainer isPhoneAlreadyUsed(int phone, ResponseContainer responseContainer){
        User user;
        try{
            user = userRepository.findByPhone(phone).orElse(null);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
        if(!ObjectUtils.isEmpty(user)){
            log.error("user already exists");
            return responseContainer.setErrorMessageAndStatusCode("user already exists",HttpStatus.BAD_REQUEST.value());
        }
        return responseContainer;
    }
}
