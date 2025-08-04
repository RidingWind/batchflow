package com.batchflow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class JobExecutionService {

    private static final Logger log = LoggerFactory.getLogger(JobExecutionService.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * This is a placeholder for a job scheduler.
     * It runs at a fixed rate to demonstrate that scheduling is active.
     * In a real implementation, this would be replaced with a more sophisticated
     * mechanism that triggers jobs based on their dependencies and status.
     */
    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void reportCurrentTime() {
        log.info("Job Scheduling Engine is active. The time is now {}", dateFormat.format(new Date()));
    }
}
