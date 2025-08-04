package com.batchflow.controller;

import com.batchflow.dto.GraphDto;
import com.batchflow.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final JobService jobService;

    public GraphController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/jobs")
    public ResponseEntity<GraphDto> getJobGraph() {
        GraphDto graph = jobService.getJobGraph();
        return ResponseEntity.ok(graph);
    }

    @GetMapping("/systems")
    public ResponseEntity<GraphDto> getSystemGraph() {
        GraphDto graph = jobService.getSystemGraph();
        return ResponseEntity.ok(graph);
    }
}
