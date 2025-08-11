package com.batchflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgeDto {
    private String source; // ID of the predecessor job
    private String target; // ID of the successor job
    private String label;  // Optional: can be used to label the edge
}
