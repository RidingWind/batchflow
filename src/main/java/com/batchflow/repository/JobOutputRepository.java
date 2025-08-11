package com.batchflow.repository;

import com.batchflow.model.JobOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOutputRepository extends JpaRepository<JobOutput, Long> {
}
