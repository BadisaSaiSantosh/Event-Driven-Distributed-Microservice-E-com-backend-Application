package com.log.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {
    private String id;
    private String serviceName;
    private String level;
    private String message;
    private String traceId;
    private LocalDateTime timestamp;
}