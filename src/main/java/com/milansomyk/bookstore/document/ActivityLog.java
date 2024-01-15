package com.milansomyk.bookstore.document;

import com.milansomyk.bookstore.dto.LogDto;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class ActivityLog {
    private int userId;
    private List<LogDto> activityLogs;

    public void addLog(LogDto logDto){
        this.activityLogs.add(logDto);
    }
}
