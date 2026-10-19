package com.log.controller;

import com.log.dto.LogDto;
import com.log.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs/v1")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    // RBAC: Only Admins can view system logs
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<LogDto>> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }

    // RBAC: Only Admins can view specific service logs
    @GetMapping("/service/{serviceName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<LogDto>> getLogsByService(@PathVariable String serviceName) {
        return ResponseEntity.ok(logService.getLogsByService(serviceName));
    }
}