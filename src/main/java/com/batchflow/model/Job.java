package com.batchflow.model;

import com.batchflow.model.enums.JobType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;
import java.util.HashSet;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String jobName;

    private String executionPath;
    private String executionMethod;
    private String description;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private com.batchflow.model.enums.JobStatus status = com.batchflow.model.enums.JobStatus.IDLE;

    private boolean manualBreakpoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_info_id")
    private SystemInfo systemInfo;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<JobInput> inputs = new HashSet<>();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<JobOutput> outputs = new HashSet<>();

    // Successors are the jobs that run after this one (post-keys)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "job_dependencies",
        joinColumns = @JoinColumn(name = "predecessor_id"),
        inverseJoinColumns = @JoinColumn(name = "successor_id")
    )
    private Set<Job> successors = new HashSet<>();

    // Predecessors are the jobs that must run before this one (pre-keys)
    // This side is managed by the 'successors' field in the predecessor jobs.
    @ManyToMany(mappedBy = "successors", fetch = FetchType.LAZY)
    private Set<Job> predecessors = new HashSet<>();
}
