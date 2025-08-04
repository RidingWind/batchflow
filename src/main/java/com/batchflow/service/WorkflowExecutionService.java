package com.batchflow.service;

import com.batchflow.model.Job;
import com.batchflow.model.enums.JobStatus;
import com.batchflow.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.batchflow.mapper.JobMapper;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WorkflowExecutionService {

    private static final Logger log = LoggerFactory.getLogger(WorkflowExecutionService.class);

    private final JobRepository jobRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final JobMapper jobMapper;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public WorkflowExecutionService(JobRepository jobRepository, SimpMessageSendingOperations messagingTemplate, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.messagingTemplate = messagingTemplate;
        this.jobMapper = jobMapper;
    }

    public void startWorkflow(Long startJobId) {
        executorService.submit(() -> {
            try {
                Job startJob = jobRepository.findById(startJobId)
                        .orElseThrow(() -> new IllegalArgumentException("Start job not found with id: " + startJobId));

                executeJob(startJob);

            } catch (Exception e) {
                log.error("Workflow execution failed", e);
            }
        });
    }

    public void rerunWorkflow(Long jobId) {
        // Placeholder for rerun logic
        log.info("Rerunning workflow from job id: {}", jobId);
        startWorkflow(jobId); // For now, rerun is the same as start
    }

    @Transactional
    public void resetJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));

        resetJobAndSuccessors(job);
    }

    private void resetJobAndSuccessors(Job job) {
        if (job.getStatus() == JobStatus.IDLE) {
            return; // Already reset
        }

        updateJobStatus(job, JobStatus.IDLE, "Job has been reset.");

        for (Job successor : job.getSuccessors()) {
            resetJobAndSuccessors(successor);
        }
    }

    public void stopJob(Long jobId) {
        // Placeholder for stop logic
        log.warn("Stop functionality is not fully implemented in this simulation.");
    }

    public void resumeJob(Long jobId) {
        // Placeholder for resume logic
        log.warn("Resume functionality is not fully implemented in this simulation.");
    }

    private void executeJob(Job job) {
        // Basic check to avoid re-running completed jobs in a simple flow
        if (job.getStatus() == JobStatus.SUCCESS) {
            log.info("Skipping already successful job: {}", job.getJobName());
            return;
        }

        // Check for predecessors
        for (Job predecessor : job.getPredecessors()) {
            if (predecessor.getStatus() != JobStatus.SUCCESS) {
                log.info("Predecessor {} of job {} is not complete. Waiting.", predecessor.getJobName(), job.getJobName());
                // In a real engine, you'd have a waiting mechanism. Here we just stop.
                return;
            }
        }

        updateJobStatus(job, JobStatus.RUNNING, "Starting execution");

        // Simulate work
        try {
            log.info("Executing job: {}", job.getJobName());
            Thread.sleep(5000); // Simulate 5 seconds of work
            updateJobStatus(job, JobStatus.SUCCESS, "Execution finished successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            updateJobStatus(job, JobStatus.FAILED, "Execution was interrupted");
        }

        // Trigger successors
        for (Job successor : job.getSuccessors()) {
            executeJob(successor);
        }
    }

    private void updateJobStatus(Job job, JobStatus status, String message) {
        log.info("Updating status of job {} to {}: {}", job.getJobName(), status, message);
        job.setStatus(status);
        jobRepository.save(job);

        // Send update to WebSocket topic
        messagingTemplate.convertAndSend("/topic/job-status", jobMapper.toDto(job));
    }
}
