package com.batchflow.dto;

import com.batchflow.model.enums.JobType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequestDto {
    private String jobName;
    private String executionPath;
    private String executionMethod;
    private String description;
    private JobType jobType;
    private boolean manualBreakpoint;
    private Long systemInfoId; // Client sends the ID of the system
    private Set<JobInputDto> inputs;
    private Set<JobOutputDto> outputs;
    private Set<String> successorJobNames; // Client sends a set of job names
}
