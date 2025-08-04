package com.batchflow.controller;

import com.batchflow.service.WorkflowExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/executions")
public class ExecutionController {

    private final WorkflowExecutionService workflowExecutionService;

    public ExecutionController(WorkflowExecutionService workflowExecutionService) {
        this.workflowExecutionService = workflowExecutionService;
    }

    @PostMapping("/start/{jobId}")
    public ResponseEntity<Void> startWorkflow(@PathVariable Long jobId) {
        workflowExecutionService.startWorkflow(jobId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/rerun/{jobId}")
    public ResponseEntity<Void> rerunWorkflow(@PathVariable Long jobId) {
        workflowExecutionService.rerunWorkflow(jobId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/reset/{jobId}")
    public ResponseEntity<Void> resetJob(@PathVariable Long jobId) {
        workflowExecutionService.resetJob(jobId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/stop/{jobId}")
    public ResponseEntity<Void> stopJob(@PathVariable Long jobId) {
        workflowExecutionService.stopJob(jobId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/resume/{jobId}")
    public ResponseEntity<Void> resumeJob(@PathVariable Long jobId) {
        workflowExecutionService.resumeJob(jobId);
        return ResponseEntity.accepted().build();
    }
}
