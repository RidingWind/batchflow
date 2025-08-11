package com.batchflow.repository;

import com.batchflow.model.SystemInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemInfoRepository extends JpaRepository<SystemInfo, Long> {
}
