package com.batchflow.model;

import com.batchflow.model.enums.JobStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobExecutionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationMs;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private Long inputCount;
    private Long outputCount;

    @Column(columnDefinition = "TEXT")
    private String log;
}
