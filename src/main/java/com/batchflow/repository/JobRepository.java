package com.batchflow.repository;

import com.batchflow.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findByJobName(String jobName);

    List<Job> findBySystemInfoId(Long systemId);

    // Eagerly fetch dependencies for the graph visualization
    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.successors LEFT JOIN FETCH j.systemInfo")
    List<Job> findAllWithSuccessors();
}
