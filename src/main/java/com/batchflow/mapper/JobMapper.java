package com.batchflow.mapper;

import com.batchflow.dto.*;
import com.batchflow.model.*;
import com.batchflow.model.enums.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.Collections;

@Component
public class JobMapper {

    public JobDto toDto(Job job) {
        if (job == null) {
            return null;
        }
        return new JobDto(
                job.getId(),
                job.getJobName(),
                job.getExecutionPath(),
                job.getExecutionMethod(),
                job.getDescription(),
                job.getJobType(),
                 job.getStatus(),
                job.isManualBreakpoint(),
                toDto(job.getSystemInfo()),
                job.getInputs().stream().map(this::toDto).collect(Collectors.toSet()),
                job.getOutputs().stream().map(this::toDto).collect(Collectors.toSet()),
                job.getSuccessors().stream().map(Job::getJobName).collect(Collectors.toSet()),
                Collections.emptySet() // Avoid lazy loading predecessors
        );
    }

    public SystemInfoDto toDto(SystemInfo systemInfo) {
        if (systemInfo == null) {
            return null;
        }
        return new SystemInfoDto(
                systemInfo.getId(),
                systemInfo.getSystemId(),
                systemInfo.getSystemName(),
                systemInfo.isNewSystem()
        );
    }

    public JobInputDto toDto(JobInput input) {
        if (input == null) {
            return null;
        }
        return new JobInputDto(input.getId(), input.getInputType(), input.getInputName(), input.isMainInput());
    }

    public JobOutputDto toDto(JobOutput output) {
        if (output == null) {
            return null;
        }
        return new JobOutputDto(output.getId(), output.getOutputType(), output.getOutputName(), output.isMainOutput());
    }

    public JobInput toEntity(JobInputDto dto) {
        if (dto == null) {
            return null;
        }
        return new JobInput(dto.getId(), dto.getInputType(), dto.getInputName(), dto.isMainInput(), null);
    }

    public JobOutput toEntity(JobOutputDto dto) {
        if (dto == null) {
            return null;
        }
        return new JobOutput(dto.getId(), dto.getOutputType(), dto.getOutputName(), dto.isMainOutput(), null);
    }
}
