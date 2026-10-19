package com.log.repository;

import com.log.entity.LogEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends MongoRepository<LogEntry, String> {
    List<LogEntry> findByServiceName(String serviceName);
    List<LogEntry> findByLevel(String level);
}