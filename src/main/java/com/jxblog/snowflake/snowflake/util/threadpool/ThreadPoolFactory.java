package com.jxblog.snowflake.snowflake.util.threadpool;

import java.util.concurrent.*;

/**
 * Existing executor service doesn't have enough rate limit mechanism,
 * Fixed thread pool: healthy policy but infinite task queue
 * cached thread: healthy policy but excessive thread
 * */
public class ThreadPoolFactory {
    public static ExecutorService fixedSizePool(int nThread, int maxTasks, String prefix
                                                         ) {
        return new ThreadPoolExecutor(
                nThread,
                nThread,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(maxTasks),
                r -> new Thread(r, prefix + "-" + r.hashCode()),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    public static ExecutorService fixedSizeAutoPool(int nThread, int maxTasks, String prefix) {
        return new ThreadPoolExecutor(
                nThread,
                nThread,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(maxTasks),
                r-> new Thread(r, prefix + "-" + r.hashCode()),
                new RejectionPolicy.CallerRunsPolicy()
        );
    }

    public static ExecutorService fixedSizeRateLimitPool(int nThread, int maxTasks, String prefix, long waitTime) {
        return new ThreadPoolExecutor(
                nThread,
                nThread,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(maxTasks),
                r -> new Thread(r, prefix + "-" + r.hashCode()),
                new RejectionPolicy.CallerBlocksPolicy(waitTime)
        );
    }
}
