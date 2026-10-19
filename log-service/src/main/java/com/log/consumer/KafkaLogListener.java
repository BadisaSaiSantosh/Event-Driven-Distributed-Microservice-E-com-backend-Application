package com.log.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.log.entity.LogEntry;
import com.log.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaLogListener {

    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "application-logs", groupId = "log-service-group")
    public void consumeLogEvent(String message) {
        try {
            // Convert the incoming JSON string from Kafka into a LogEntry object
            LogEntry logEntry = objectMapper.readValue(message, LogEntry.class);
            
            // Save to MongoDB
            logRepository.save(logEntry);
            
            log.debug("Successfully saved log from service: {}", logEntry.getServiceName());
        } catch (Exception e) {
            log.error("Failed to process log message: {}", message, e);
        }
    }
}