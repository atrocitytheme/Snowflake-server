package com.jxblog.snowflake.snowflake.util.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

public interface RejectionPolicy extends RejectedExecutionHandler {
    class CallerBlocksPolicy implements RejectionPolicy {
        private static final Logger s_logger = LogManager.getLogger(CallerBlocksPolicy.class);
        private long waitTime;

        public CallerBlocksPolicy(long waitTime) {
            this.waitTime = waitTime;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (!executor.isShutdown()) {
                BlockingQueue<Runnable> curQ = executor.getQueue();
                try {
                    s_logger.debug("Attempting to queue the task in " + this.waitTime + "millesecond");
                    if (!curQ.offer(r, this.waitTime, TimeUnit.MILLISECONDS)) {
                        throw new RejectedExecutionException("task engagement failed!");
                    };
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RejectedExecutionException("Thread: " + Thread.currentThread().getName() + " Interrupted due to", e);
                }
            }

            else {
                throw new RejectedExecutionException("Executor not valid anymore");
            }
        }
    }

    class CallerRunsPolicy extends ThreadPoolExecutor.CallerRunsPolicy implements RejectionPolicy {}
}
