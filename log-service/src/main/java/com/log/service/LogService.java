package com.log.service;

import com.log.dto.LogDto;
import com.log.entity.LogEntry;
import java.util.List;

public interface LogService {
    void saveLog(LogEntry logEntry); // Used by Kafka
    List<LogDto> getAllLogs();       // Used by Controller
    List<LogDto> getLogsByService(String serviceName);
}