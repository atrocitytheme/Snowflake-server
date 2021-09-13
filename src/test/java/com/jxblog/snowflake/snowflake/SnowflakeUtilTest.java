package com.jxblog.snowflake.snowflake;

import com.jxblog.snowflake.snowflake.snowflake.SnowInformation;
import com.jxblog.snowflake.snowflake.snowflake.Snowflake;
import com.jxblog.snowflake.snowflake.util.SnowflakeUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class SnowflakeUtilTest {
    private long left_timestamp_shift = Snowflake.DATA_CENTER_BITS + Snowflake.SEQUENCE_BITS + Snowflake.WORKER_ID_BITS;
    private long left_datacenter_shift = left_timestamp_shift - Snowflake.DATA_CENTER_BITS;
    private long left_worker_shift = left_datacenter_shift - Snowflake.WORKER_ID_BITS;

    @Test
    public void testSnowInformationParsing() {
        long testSeq1 = buildSnowId(231L, 3L, 5L, 2L);
        SnowInformation info = SnowflakeUtils.parse(testSeq1);
        Assert.assertEquals(231L + Snowflake.epoch, info.getTimestamp());
        Assert.assertEquals(3L, info.getDatacenterId());
        Assert.assertEquals(5L, info.getWorkerId());
        Assert.assertEquals(2L, info.getSequence());
    }

    private long buildSnowId(long timestamp, long datacenter, long worker, long seq) {
        return (timestamp << left_timestamp_shift) |
                (datacenter << left_datacenter_shift) |
                (worker << left_worker_shift) |
                (seq);
    }
}
