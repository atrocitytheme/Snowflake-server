package com.jxblog.snowflake.snowflake.snowflake;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SnowflakeMachineInfo {
    @NotNull(message = "machine Id should not be empty")
    private long machineId; // workerId in snowflake
    @NotNull(message = "ip should not be empty")
    private String ip;
    @NotNull(message = "datacenter name should not be empty")
    private String dataCenteName;
    @NotNull(message = "datacenter Id should not be empty")
    private long dataCenterId;
    @NotNull(message = "machine name should not be empty")
    private String machineName;
    private String description;
    private Boolean deleted;
}
