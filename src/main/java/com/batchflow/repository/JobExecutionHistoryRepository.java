package com.batchflow.repository;

import com.batchflow.model.JobExecutionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobExecutionHistoryRepository extends JpaRepository<JobExecutionHistory, Long> {

    List<JobExecutionHistory> findByJobIdOrderByStartTimeDesc(Long jobId);

    // This can be used later to get the last N runs for the UI
    List<JobExecutionHistory> findFirst3ByJobIdOrderByStartTimeDesc(Long jobId);
}
