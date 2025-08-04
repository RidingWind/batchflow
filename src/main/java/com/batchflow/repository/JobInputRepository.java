package com.batchflow.repository;

import com.batchflow.model.JobInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobInputRepository extends JpaRepository<JobInput, Long> {
}
