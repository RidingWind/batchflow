package com.batchflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemInfoDto {
    private Long id;
    private String systemId;
    private String systemName;
    private boolean isNewSystem;
}
