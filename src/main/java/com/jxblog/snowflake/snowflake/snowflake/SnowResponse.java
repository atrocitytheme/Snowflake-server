package com.jxblog.snowflake.snowflake.snowflake;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SnowResponse {
    public SnowResponse(long snowId, Date createdDate) {
        this.snowflakeId = snowId;
        this.createdDate = createdDate;
    }
    public SnowResponse(long snowId, Date createdDate, String information) {
        this(snowId, createdDate);
        this.errorMsg = information;
    }
    private long snowflakeId;
    private Date createdDate;
    private String errorMsg;
}
