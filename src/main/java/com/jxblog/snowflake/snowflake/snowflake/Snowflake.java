package com.jxblog.snowflake.snowflake.snowflake;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
/**
 * prepare kafka (major restoration for recovery jobs and snowflake population)
 * API required (HTTP for query) and gRPC for service call
 * */
public final class Snowflake {
    /**
     * According to snowflake documentation, one bit is for sign and is invalid
     * */
    public static final long UNUSED_BITS = 1L;
    public static final long TIMESTAMP_BITS = 41L;
    public static final long DATA_CENTER_BITS = 5L;
    public static final long WORKER_ID_BITS = 5L;
    public static final long SEQUENCE_BITS = 12L;
    public static final long epoch = 1631431843748L;

    private static long maxDatacenterId = -1L ^ (-1L << DATA_CENTER_BITS);

    private static long maxWorkerId = -1L ^ (-1L << WORKER_ID_BITS);

    private static long maxSequence = -1L ^ (-1L << SEQUENCE_BITS);

    private static long leftShiftTimestamp = SEQUENCE_BITS + DATA_CENTER_BITS + WORKER_ID_BITS;

    private static long leftShiftDatacenter = leftShiftTimestamp - DATA_CENTER_BITS;

    private static long leftShiftWorker = leftShiftDatacenter - WORKER_ID_BITS;

    private final long workerId;

    private final long dataCenterId;

    private long sequence = 0L;

    private long lastTimeStamp = -1L;

    private String name;

    /**
     * stats variable: count the total number of time inconsistency
     * {@link #waitNextMilliseconds(long)}
     * */
    private final AtomicLong waitCount = new AtomicLong(0);

    public AtomicLong getWaitCount() {
        return waitCount;
    }

    public Snowflake(long workerId, long dataCenterId) {
        if (dataCenterId > maxDatacenterId || dataCenterId < 0) {
            throw new IllegalArgumentException("Datacenter id should be at most: " + maxDatacenterId);
        }
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("Worker id should be at most " + maxWorkerId);
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.name = "default-" + new Date(currentTime());
    }

    public Snowflake(long workderId, long dataCenterId, String name) {
        this(workderId, dataCenterId);
        this.name = name;
    }
    /**
     * generate the incremental Id
     * @return new id
     * */
    public synchronized long nextID() {
        long curStamp = currentTime();
        if (curStamp < lastTimeStamp) {
            throw new IllegalStateException("CLock moved backwards, need to resync, time lap: "+ (lastTimeStamp - curStamp) + "ms");
        }
        if (curStamp == lastTimeStamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                curStamp = waitNextMilliseconds(curStamp);
            }
        } else {
            // once the time is different, no need to specify with sequence
            sequence = 0L;
        }

        lastTimeStamp = curStamp;
        return (curStamp - epoch) << leftShiftTimestamp |
                (dataCenterId << leftShiftDatacenter) |
                (workerId << leftShiftWorker) |
                sequence;
    }

    private long waitNextMilliseconds(long curTime) {
        waitCount.getAndIncrement();
        while (curTime <= lastTimeStamp) {
            curTime = currentTime();
        }
        return curTime;
    }

    public long currentTime() {
        return System.currentTimeMillis();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Snowflake: %s [timeBits=%d, " +
                "datacenterBits=%d, " +
                "workerIdBits=%d, " +
                "sequenceBits=%d," +
                "epoch=%d]" +
                "\n dataCenterId=%d, workerId=%d",
                this.name, TIMESTAMP_BITS,
                DATA_CENTER_BITS,
                WORKER_ID_BITS,
                SEQUENCE_BITS,
                epoch,
                dataCenterId,
                workerId);
    }
}
