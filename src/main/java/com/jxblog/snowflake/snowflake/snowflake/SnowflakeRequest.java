package com.jxblog.snowflake.snowflake.snowflake;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SnowflakeRequest {
    @NotNull
    @JsonProperty("worker_id")
    private long workerId;

    @NotNull
    @JsonProperty("datacenter_id")
    private long dataCenterId;

    @NotNull
    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("identifier")
    @NotNull
    private String identifier;

    @JsonIgnore
    private boolean passed;
}
