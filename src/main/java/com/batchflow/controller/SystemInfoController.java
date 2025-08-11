package com.batchflow.controller;

import com.batchflow.dto.SystemInfoDto;
import com.batchflow.model.SystemInfo;
import com.batchflow.dto.JobDto;
import com.batchflow.repository.SystemInfoRepository;
import com.batchflow.mapper.JobMapper;
import com.batchflow.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/systems")
public class SystemInfoController {

    private final SystemInfoRepository systemInfoRepository;
    private final JobService jobService;
    private final JobMapper jobMapper;

    public SystemInfoController(SystemInfoRepository systemInfoRepository, JobService jobService, JobMapper jobMapper) {
        this.systemInfoRepository = systemInfoRepository;
        this.jobService = jobService;
        this.jobMapper = jobMapper;
    }

    @GetMapping("/{id}/jobs")
    public ResponseEntity<List<JobDto>> getJobsBySystem(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobsBySystemId(id));
    }

    @PostMapping
    public ResponseEntity<SystemInfoDto> createSystem(@RequestBody SystemInfoDto systemInfoDto) {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setSystemId(systemInfoDto.getSystemId());
        systemInfo.setSystemName(systemInfoDto.getSystemName());
        systemInfo.setNewSystem(systemInfoDto.isNewSystem());
        SystemInfo savedSystem = systemInfoRepository.save(systemInfo);
        return ResponseEntity.ok(jobMapper.toDto(savedSystem));
    }

    @GetMapping
    public ResponseEntity<List<SystemInfoDto>> getAllSystems() {
        List<SystemInfoDto> systems = systemInfoRepository.findAll().stream()
                .map(jobMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(systems);
    }
}
