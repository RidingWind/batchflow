package com.batchflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphDto {
    private List<NodeDto> nodes;
    private List<EdgeDto> edges;
}
