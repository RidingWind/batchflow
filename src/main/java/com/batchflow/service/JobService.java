package com.batchflow.service;

import com.batchflow.dto.JobDto;
import com.batchflow.dto.JobRequestDto;
import com.batchflow.mapper.JobMapper;
import com.batchflow.model.Job;
import com.batchflow.model.JobInput;
import com.batchflow.model.JobOutput;
import com.batchflow.model.SystemInfo;
import com.batchflow.repository.JobRepository;
import com.batchflow.repository.SystemInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import com.batchflow.dto.EdgeDto;
import com.batchflow.dto.GraphDto;
import com.batchflow.dto.NodeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SystemInfoRepository systemInfoRepository;
    private final JobMapper jobMapper;

    public JobService(JobRepository jobRepository, SystemInfoRepository systemInfoRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.systemInfoRepository = systemInfoRepository;
        this.jobMapper = jobMapper;
    }

    @Transactional
    public JobDto createJob(JobRequestDto jobRequestDto) {
        Job job = new Job();
        mapRequestDtoToEntity(jobRequestDto, job);
        Job savedJob = jobRepository.save(job);
        return jobMapper.toDto(savedJob);
    }

    @Transactional(readOnly = true)
    public List<JobDto> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(jobMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JobDto> getJobsBySystemId(Long systemId) {
        return jobRepository.findBySystemInfoId(systemId).stream()
                .map(jobMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JobDto getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + id));
        return jobMapper.toDto(job);
    }

    @Transactional
    public JobDto updateJob(Long id, JobRequestDto jobRequestDto) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + id));

        mapRequestDtoToEntity(jobRequestDto, job);
        Job updatedJob = jobRepository.save(job);
        return jobMapper.toDto(updatedJob);
    }

    @Transactional
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + id));

        if (!job.getPredecessors().isEmpty()) {
            throw new IllegalStateException("Cannot delete job with id " + id + " because it is a successor (post-key) to other jobs.");
        }

        jobRepository.delete(job);
    }

    @Transactional(readOnly = true)
    public GraphDto getJobGraph() {
        List<Job> jobs = jobRepository.findAllWithSuccessors();
        List<NodeDto> nodes = new ArrayList<>();
        List<EdgeDto> edges = new ArrayList<>();

        for (Job job : jobs) {
            nodes.add(new NodeDto(String.valueOf(job.getId()), job.getJobName(), job.getJobType().name()));
            for (Job successor : job.getSuccessors()) {
                edges.add(new EdgeDto(String.valueOf(job.getId()), String.valueOf(successor.getId()), ""));
            }
        }
        return new GraphDto(nodes, edges);
    }

    @Transactional(readOnly = true)
    public GraphDto getSystemGraph() {
        List<Job> jobs = jobRepository.findAllWithSuccessors();
        List<NodeDto> systemNodes = new ArrayList<>();
        List<EdgeDto> systemEdges = new ArrayList<>();
        Set<String> systemNodeIds = new HashSet<>();
        Set<String> edgeKeys = new HashSet<>();

        for (Job job : jobs) {
            SystemInfo sourceSystem = job.getSystemInfo();
            if (sourceSystem != null && systemNodeIds.add(sourceSystem.getSystemId())) {
                systemNodes.add(new NodeDto(sourceSystem.getSystemId(), sourceSystem.getSystemName(), "SYSTEM"));
            }

            for (Job successor : job.getSuccessors()) {
                SystemInfo targetSystem = successor.getSystemInfo();
                if (targetSystem != null && systemNodeIds.add(targetSystem.getSystemId())) {
                    systemNodes.add(new NodeDto(targetSystem.getSystemId(), targetSystem.getSystemName(), "SYSTEM"));
                }

                if (sourceSystem != null && targetSystem != null && !sourceSystem.getId().equals(targetSystem.getId())) {
                    String edgeKey = sourceSystem.getSystemId() + "->" + targetSystem.getSystemId();
                    if (edgeKeys.add(edgeKey)) {
                        systemEdges.add(new EdgeDto(sourceSystem.getSystemId(), targetSystem.getSystemId(), "Dependency"));
                    }
                }
            }
        }
        return new GraphDto(systemNodes, systemEdges);
    }

    private void mapRequestDtoToEntity(JobRequestDto dto, Job job) {
        job.setJobName(dto.getJobName());
        job.setExecutionPath(dto.getExecutionPath());
        job.setExecutionMethod(dto.getExecutionMethod());
        job.setDescription(dto.getDescription());
        job.setJobType(dto.getJobType());
        job.setManualBreakpoint(dto.isManualBreakpoint());

        // Set SystemInfo
        SystemInfo systemInfo = systemInfoRepository.findById(dto.getSystemInfoId())
                .orElseThrow(() -> new EntityNotFoundException("SystemInfo not found with id: " + dto.getSystemInfoId()));
        job.setSystemInfo(systemInfo);

        // Set Inputs
        job.getInputs().clear();
        if (dto.getInputs() != null) {
            Set<JobInput> inputs = dto.getInputs().stream()
                    .map(jobMapper::toEntity)
                    .peek(input -> input.setJob(job))
                    .collect(Collectors.toSet());
            job.getInputs().addAll(inputs);
        }

        // Set Outputs
        job.getOutputs().clear();
        if (dto.getOutputs() != null) {
            Set<JobOutput> outputs = dto.getOutputs().stream()
                    .map(jobMapper::toEntity)
                    .peek(output -> output.setJob(job))
                    .collect(Collectors.toSet());
            job.getOutputs().addAll(outputs);
        }

        // Set Successors
        job.getSuccessors().clear();
        if (dto.getSuccessorJobNames() != null) {
            Set<Job> successors = new HashSet<>();
            for (String successorName : dto.getSuccessorJobNames()) {
                Job successorJob = jobRepository.findByJobName(successorName)
                        .orElseThrow(() -> new EntityNotFoundException("Successor job not found with name: " + successorName));
                successors.add(successorJob);
            }
            job.setSuccessors(successors);
        }
    }
}
