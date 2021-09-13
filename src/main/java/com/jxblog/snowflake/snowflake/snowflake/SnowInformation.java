package com.jxblog.snowflake.snowflake.snowflake;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SnowInformation {

    public SnowInformation(long timestamp, long dataCenter, long workerId, long seq) {
        this.timestamp = timestamp;
        this.datacenterId = dataCenter;
        this.workerId = workerId;
        this.sequence = seq;
    }

    private long timestamp;
    private long datacenterId;
    private long workerId;
    private long sequence;

    @Override
    public String toString() {
        return String.format("SnowInofation[timestamp: %d, datacenterId: %d, workerId: %d, sequence: %d]",
                timestamp, datacenterId, workerId, sequence);
    }
}
