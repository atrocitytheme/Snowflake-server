package com.jxblog.snowflake.snowflake.util;

import com.jxblog.snowflake.snowflake.snowflake.SnowInformation;
import com.jxblog.snowflake.snowflake.snowflake.Snowflake;

public class SnowflakeUtils {
    /**
     * get the snowflake information from the snowId generated
     * */
    public static SnowInformation parse(long snowId) {
        long timestampShift = Snowflake.DATA_CENTER_BITS + Snowflake.SEQUENCE_BITS + Snowflake.WORKER_ID_BITS;
        long dataCenterShift = timestampShift - Snowflake.DATA_CENTER_BITS;
        long workerShift = dataCenterShift - Snowflake.WORKER_ID_BITS;

        long timestamp = (snowId &
                          zeroMargin(Snowflake.UNUSED_BITS, Snowflake.TIMESTAMP_BITS)
                        ) >> timestampShift;
        timestamp += Snowflake.epoch;
        long datacenterId = (snowId &
                zeroMargin(Snowflake.UNUSED_BITS + Snowflake.TIMESTAMP_BITS, Snowflake.DATA_CENTER_BITS)
                ) >> dataCenterShift;

        long workerId = (snowId &
                        zeroMargin(Snowflake.UNUSED_BITS + Snowflake.TIMESTAMP_BITS +
                                    Snowflake.DATA_CENTER_BITS, Snowflake.WORKER_ID_BITS)
                         ) >> workerShift;
        long sequence = (snowId &
                        zeroMargin(Snowflake.UNUSED_BITS + Snowflake.TIMESTAMP_BITS
                                    + Snowflake.DATA_CENTER_BITS + Snowflake.WORKER_ID_BITS, Snowflake.SEQUENCE_BITS)
                        );
        return new SnowInformation(timestamp, datacenterId, workerId, sequence);
    }

    /**
     * Process the long value to be margin filled with zero
     * leftMarginPos + length = rightMarginPos
     * @return wrapped long with margin zero-filled
     * e.g: 01111....1000 leftMarginPos: 1, length: 60
     * */
    private static long zeroMargin(long leftMarginPos, long length) {
        int ml = (int) (64 - leftMarginPos);
        int mr = (int) (64 - (leftMarginPos + length));
        return (-1L << ml) ^ (-1L << mr);
    }
}
