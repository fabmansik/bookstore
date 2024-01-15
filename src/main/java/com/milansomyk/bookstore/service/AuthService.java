package com.milansomyk.bookstore.service;

import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.dto.requests.RefreshRequest;
import com.milansomyk.bookstore.dto.requests.SignInRequest;
import com.milansomyk.bookstore.dto.responses.JwtResponse;
import com.milansomyk.bookstore.entity.User;
import com.milansomyk.bookstore.enums.LogType;
import com.milansomyk.bookstore.mapper.UserMapper;
import com.milansomyk.bookstore.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
@Data
@Slf4j
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final DbUserDetailsService dbUserDetailsService;
    private final ActivityLogService activityLogService;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    public ResponseContainer login (SignInRequest signInRequest){
        ResponseContainer responseContainer = new ResponseContainer();
        try{
             Authentication authentication = UsernamePasswordAuthenticationToken
                    .unauthenticated(signInRequest.getUsername(),signInRequest.getPassword());
             authenticationManager.authenticate(authentication);
        } catch (AuthenticationException e){
            log.error(e.getMessage());
            return  responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
        UserDetails userDetails;
        try{
            userDetails = dbUserDetailsService.loadUserByUsername(signInRequest.getUsername());
        } catch ( Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
        String username = userDetails.getUsername();
        User user;
        try{
            user = userRepository.findByUsername(username).orElse(null);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if (ObjectUtils.isEmpty(user)){
            log.error("username not found");
            return responseContainer.setErrorMessageAndStatusCode("username not found",HttpStatus.BAD_REQUEST.value());
        }
        String token;
        try{
            token = jwtService.generateToken(userDetails);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        String refresh;
        try{
            refresh = jwtService.generateRefreshToken(userDetails);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        ResponseContainer logged = activityLogService.log(user.getUserId(), LogType.LOGINATION);
        if (logged.isError()){
            log.error(logged.getErrorMessage());
            return logged;
        }
        return responseContainer.setSuccessResult(new JwtResponse(token, refresh));
    }
    public ResponseContainer refresh(RefreshRequest refreshRequest){
        ResponseContainer responseContainer = new ResponseContainer();
        if(!StringUtils.hasText(refreshRequest.getRefresh())){
            log.error("refresh is null");
            return responseContainer.setErrorMessageAndStatusCode("refresh is null",HttpStatus.BAD_REQUEST.value());
        }
        String refreshToken = refreshRequest.getRefresh();
        if(!jwtService.isRefreshType(refreshRequest.getRefresh())) {
            log.error("not refresh token");
            return responseContainer.setErrorMessageAndStatusCode("not refresh token",HttpStatus.BAD_REQUEST.value());
        }
        if(jwtService.isTokenExpired(refreshToken)){
            log.error("refresh token expired");
            return responseContainer.setErrorMessageAndStatusCode("refresh token expired",HttpStatus.BAD_REQUEST.value());
        }
        String username;
        try{
            username = jwtService.extractUsername(refreshToken);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(!StringUtils.hasText(username)){
            log.error("username is null");
            return responseContainer.setErrorMessageAndStatusCode("username is null", HttpStatus.BAD_REQUEST.value());
        }
        UserDetails userDetails;
        try{
            userDetails = dbUserDetailsService.loadUserByUsername(username);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(userDetails)){
            log.error("user details are null");
            return responseContainer.setErrorMessageAndStatusCode("user details are null", HttpStatus.BAD_REQUEST.value());
        }
        String access;
        try{
            access = jwtService.generateToken(userDetails);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        boolean isNeedNew;
        try{
            isNeedNew = jwtService.extractDuration(refreshToken).toHours()<12;
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(isNeedNew){
            String newRefresh;
            try{
                newRefresh = jwtService.generateRefreshToken(userDetails);
            } catch (Exception e){
                log.error(e.getMessage());
                return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            return responseContainer.setSuccessResult(new JwtResponse(access, newRefresh));
        }
        return responseContainer.setSuccessResult(new JwtResponse(access, refreshToken));
    }
    public String extractUsernameFromAuth(String auth){
        String token = jwtService.extractTokenFromAuth(auth);
        return jwtService.extractUsername(token);
    }
}
