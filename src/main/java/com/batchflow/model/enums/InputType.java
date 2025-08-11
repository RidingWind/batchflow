package com.batchflow.model.enums;

public enum InputType {
    OLD_SYSTEM_DATA_TABLE,      // 旧线数据表
    INTERMEDIATE_TABLE,         // 中间表
    NEW_SYSTEM_TARGET_TABLE,    // 新线目标表
    EXPORTED_REPORT_DATA,       // 迁出报表数据
    IMPORTED_REPORT_DATA,       // 迁入报表数据
    UNLOADED_DATA_FILE,         // 卸数文件
    STATISTICS_FILE             // 统计文件
}
