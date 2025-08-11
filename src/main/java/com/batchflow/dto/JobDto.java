package com.batchflow.dto;

import com.batchflow.model.enums.JobType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {
    private Long id;
    private String jobName;
    private String executionPath;
    private String executionMethod;
    private String description;
    private JobType jobType;
    private com.batchflow.model.enums.JobStatus status;
    private boolean manualBreakpoint;
    private SystemInfoDto systemInfo;
    private Set<JobInputDto> inputs;
    private Set<JobOutputDto> outputs;
    private Set<String> successorJobNames;
    private Set<String> predecessorJobNames;
}
