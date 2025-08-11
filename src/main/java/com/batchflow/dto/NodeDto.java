package com.batchflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeDto {
    private String id;
    private String label;
    private String type; // e.g., JobType enum name or "SYSTEM"
}
