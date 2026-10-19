package com.log.service;

import com.log.dto.LogDto;
import com.log.entity.LogEntry;
import com.log.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Override
    public void saveLog(LogEntry logEntry) {
        logRepository.save(logEntry);
    }

    @Override
    public List<LogDto> getAllLogs() {
        return logRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LogDto> getLogsByService(String serviceName) {
        return logRepository.findByServiceName(serviceName).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private LogDto mapToDto(LogEntry entry) {
        return LogDto.builder()
                .id(entry.getId())
                .serviceName(entry.getServiceName())
                .level(entry.getLevel())
                .message(entry.getMessage())
                .traceId(entry.getTraceId())
                .timestamp(entry.getTimestamp())
                .build();
    }
}