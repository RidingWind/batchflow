package com.batchflow.model.enums;

public enum OutputType {
    INTERMEDIATE_TABLE,         // 中间表
    NEW_SYSTEM_TARGET_TABLE,    // 新线目标表
    VALIDATION_RESULT_TABLE,    // 检核结果表
    VALIDATION_REPORT,          // 检核报表
    STATISTICS_TABLE,           // 统计表
    UNLOADED_DATA_FILE          // 卸数文件
}
