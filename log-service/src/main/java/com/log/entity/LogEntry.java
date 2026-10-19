package com.log.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "system_logs")
public class LogEntry {
    @Id
    private String id;
    private String serviceName; // e.g., "order-service"
    private String level;       // e.g., "INFO", "ERROR"
    private String message;     // The actual log message
    private String traceId;     // For tracking requests across microservices

    @CreatedDate
    private LocalDateTime timestamp;
}