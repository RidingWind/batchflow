package com.batchflow.model.enums;

public enum JobStatus {
    IDLE,       // Not running, waiting for trigger
    RUNNING,    // Actively executing
    SUCCESS,    // Completed successfully
    FAILED,     // Execution failed
    PAUSED,     // Manually paused at a breakpoint
    STOPPED     // Manually stopped by a user
}
