package com.jxblog.snowflake.snowflake.context;

public interface IdempotentChecker <K, T> {
    boolean isRepeatedOperation(K serviceName, T uuid);
    void set(K serviceName, T value) throws IllegalStateException;
    void release(K serviceName, T uuid);

}
