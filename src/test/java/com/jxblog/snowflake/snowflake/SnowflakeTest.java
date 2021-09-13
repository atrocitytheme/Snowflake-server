package com.jxblog.snowflake.snowflake;

import com.jxblog.snowflake.snowflake.context.ProcessContext;
import com.jxblog.snowflake.snowflake.snowflake.SnowInformation;
import com.jxblog.snowflake.snowflake.snowflake.Snowflake;
import com.jxblog.snowflake.snowflake.util.SnowflakeUtils;
import com.jxblog.snowflake.snowflake.util.threadpool.ThreadPoolFactory;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class SnowflakeTest {
    private static int THEAD_MNA = 10;
    private ExecutorService threadPool;
    private Snowflake snowflake;
    @BeforeEach
    void initThreadPool() {

        threadPool = ThreadPoolFactory.fixedSizePool(THEAD_MNA, 5000, "SnowflakeTest");
        snowflake = new Snowflake(5L, 3L, "SnowflakeTest");
    }

    @AfterEach
    void tearDown() {
        threadPool.shutdownNow();
        threadPool = null;
    }

    @Test
    public void testSnowFlakeIdGenerationAbility() throws InterruptedException {
        long id1 = snowflake.nextID();
        Thread.sleep(1);
        long id2 = snowflake.nextID();
        Assert.assertNotEquals(id1, id2);

        SnowInformation info1 = SnowflakeUtils.parse(id1);
        SnowInformation info2 = SnowflakeUtils.parse(id2);

        Assert.assertNotEquals(info1.getTimestamp(), info2.getTimestamp());
        Assert.assertEquals(info1.getWorkerId(), info2.getWorkerId());
        Assert.assertEquals(info1.getDatacenterId(), info2.getDatacenterId());
        Assert.assertEquals(info1.getSequence(), info2.getSequence());
    }

    @Test
    @Timeout(5)
    public void testSnowFlakeMultiThreadingAbility() throws InterruptedException {
        int taskNum = 1000;
        final List<Long> snowIds = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latchForAwait = new CountDownLatch(taskNum);
        for (int i = 0; i < 1000; i ++) {
            threadPool.execute(()-> {
                snowIds.add(snowflake.nextID());
                latchForAwait.countDown();
            });
        }
        latchForAwait.await();
        boolean equals = false;
        Set<Long> numColection = new HashSet<>(snowIds);
        Assert.assertEquals(taskNum, numColection.size());
    }

    @Test
    @Timeout(120)
    public void testProcessContextMultiThreadingAbility() throws InterruptedException {
        final int taskNum = 500_0000;
        threadPool = ThreadPoolFactory.fixedSizeAutoPool(100, taskNum, "test_processContext_small");
        long shifter = (1L) << 8 - 1;
        CountDownLatch latchForAwait = new CountDownLatch(taskNum);
        final ProcessContext<String, String> ctx = new ProcessContext<>();
        for (int i = 0; i < taskNum; i++) {
            final int tmp = i;
            threadPool.execute(() -> {
                ctx.set("test1", String.valueOf(tmp));
                latchForAwait.countDown();
            });
        }
        latchForAwait.await();
        Assert.assertTrue(ctx.isRepeatedOperation("test1", "5671"));
    }
}
