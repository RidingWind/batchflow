package com.batchflow.dto;

import com.batchflow.model.enums.InputType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInputDto {
    private Long id;
    private InputType inputType;
    private String inputName;
    private boolean isMainInput;
}
