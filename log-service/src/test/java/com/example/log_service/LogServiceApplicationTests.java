package com.example.log_service;
import com.log.consumer.KafkaLogListener; 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.log.entity.LogEntry;
import com.log.repository.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaLogListenerTest {

    @Mock
    private LogRepository logRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaLogListener kafkaLogListener;

    private String validJsonMessage;
    private LogEntry mockLogEntry;

    @BeforeEach
    void setUp() {
        validJsonMessage = "{\"serviceName\":\"order-service\",\"level\":\"ERROR\",\"message\":\"Payment failed\",\"traceId\":\"12345\"}";
        
        mockLogEntry = LogEntry.builder()
                .serviceName("order-service")
                .level("ERROR")
                .message("Payment failed")
                .traceId("12345")
                .build();
    }

    @Test
    void testConsumeLogEvent_Success() throws Exception {
        // Arrange: Tell the mock ObjectMapper to return our mock object when it reads the string
        when(objectMapper.readValue(validJsonMessage, LogEntry.class)).thenReturn(mockLogEntry);

        // Act: Call the listener method
        kafkaLogListener.consumeLogEvent(validJsonMessage);

        // Assert: Verify that repository.save() was called exactly once
        ArgumentCaptor<LogEntry> logCaptor = ArgumentCaptor.forClass(LogEntry.class);
        verify(logRepository, times(1)).save(logCaptor.capture());

        // Verify the saved object has the correct data
        LogEntry savedLog = logCaptor.getValue();
        assertEquals("order-service", savedLog.getServiceName());
        assertEquals("ERROR", savedLog.getLevel());
        assertEquals("Payment failed", savedLog.getMessage());
    }

    @Test
    void testConsumeLogEvent_ExceptionHandling() throws Exception {
        // Arrange: Force the ObjectMapper to throw an exception (e.g., bad JSON)
        String badJsonMessage = "Invalid JSON";
        when(objectMapper.readValue(badJsonMessage, LogEntry.class)).thenThrow(new RuntimeException("Parsing error"));

        // Act: Call the listener method with bad data
        kafkaLogListener.consumeLogEvent(badJsonMessage);

        // Assert: Verify that repository.save() was NEVER called because it caught the error
        verify(logRepository, never()).save(any(LogEntry.class));
    }
}