package com.batchflow.dto;

import com.batchflow.model.enums.OutputType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOutputDto {
    private Long id;
    private OutputType outputType;
    private String outputName;
    private boolean isMainOutput;
}
