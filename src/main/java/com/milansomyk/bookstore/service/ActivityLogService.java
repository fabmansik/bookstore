package com.milansomyk.bookstore.service;

import com.milansomyk.bookstore.document.ActivityLog;
import com.milansomyk.bookstore.dto.LogDetails;
import com.milansomyk.bookstore.dto.LogDto;
import com.milansomyk.bookstore.dto.ResponseContainer;
import com.milansomyk.bookstore.entity.User;
import com.milansomyk.bookstore.enums.LogType;
import com.milansomyk.bookstore.repository.ActivityLogRepository;
import com.milansomyk.bookstore.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Data
@Slf4j
public class ActivityLogService {
    private final UserRepository userRepository;
    private final ActivityLogRepository activityLogRepository;
    public ResponseContainer init(int userId){
        ActivityLog activityLog = new ActivityLog();
        ResponseContainer responseContainer = new ResponseContainer();
        if(ObjectUtils.isEmpty(userId)){
            log.error("user id is null");
            return responseContainer.setErrorMessageAndStatusCode("user id is null", HttpStatus.BAD_REQUEST.value());
        }
        User found;
        try{
            found = userRepository.findById(userId).orElse(null);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(found)){
            log.error("user not found");
            return responseContainer.setErrorMessageAndStatusCode("user not found",HttpStatus.BAD_REQUEST.value());
        }
        activityLog.setUserId(userId);
        LogDto logDto = new LogDto();
        logDto.setActivityType(LogType.REGISTRATION.toString());
        logDto.setDetails("");
        logDto.setTimestamp(new Date(System.currentTimeMillis()));
        activityLog.setActivityLogs(new ArrayList<>());
        activityLog.addLog(logDto);
        try {
            activityLogRepository.save(activityLog);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseContainer.setSuccessResult(activityLog);
    }

    public ResponseContainer log(int userId, List<Integer> bookIds, LogType type){
        ResponseContainer responseContainer = new ResponseContainer();
        ActivityLog activityLog;
        try {
            activityLog = activityLogRepository.findById(userId).orElse(null);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(activityLog)){
            log.error("logs not found");
            return responseContainer.setErrorMessageAndStatusCode("logs not found",HttpStatus.BAD_REQUEST.value());
        }
        activityLog.setUserId(userId);
        LogDto logDto = new LogDto();
        logDto.setActivityType(type.toString());
        LogDetails logDetails = new LogDetails();
        logDetails.setBooksIds(bookIds);
        logDto.setDetails(logDetails);
        logDto.setTimestamp(new Date(System.currentTimeMillis()));
        activityLog.setActivityLogs(new ArrayList<>());
        activityLog.addLog(logDto);
        try {
            activityLogRepository.save(activityLog);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseContainer.setSuccessResult(activityLog);
    }
    public ResponseContainer log(int userId, int bookId, LogType type){
        ResponseContainer responseContainer = new ResponseContainer();
        ActivityLog activityLog;
        try {
            activityLog = activityLogRepository.findById(userId).orElse(null);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(activityLog)){
            log.error("logs not found");
            return responseContainer.setErrorMessageAndStatusCode("logs not found",HttpStatus.BAD_REQUEST.value());
        }
        activityLog.setUserId(userId);
        LogDto logDto = new LogDto();
        logDto.setActivityType(type.toString());
        LogDetails logDetails = new LogDetails();
        logDetails.setBookId(bookId);
        logDto.setDetails(logDetails);
        logDto.setTimestamp(new Date(System.currentTimeMillis()));
        activityLog.setActivityLogs(new ArrayList<>());
        activityLog.addLog(logDto);
        try {
            activityLogRepository.save(activityLog);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseContainer.setSuccessResult(activityLog);
    }
    public ResponseContainer log(int userId, LogType type){
        ResponseContainer responseContainer = new ResponseContainer();
        ActivityLog activityLog;
        try {
            activityLog = activityLogRepository.findById(userId).orElse(null);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(ObjectUtils.isEmpty(activityLog)){
            log.error("logs not found");
            return responseContainer.setErrorMessageAndStatusCode("logs not found",HttpStatus.BAD_REQUEST.value());
        }
        activityLog.setUserId(userId);
        LogDto logDto = new LogDto();
        logDto.setActivityType(type.toString());
        logDto.setTimestamp(new Date(System.currentTimeMillis()));
        activityLog.setActivityLogs(new ArrayList<>());
        activityLog.addLog(logDto);
        try {
            activityLogRepository.save(activityLog);
        } catch (Exception e){
            log.error(e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseContainer.setSuccessResult(activityLog);
    }
}
