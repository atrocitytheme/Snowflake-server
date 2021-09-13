package com.jxblog.snowflake.snowflake.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ProcessContext<K, T> implements IdempotentChecker<K, T> {
    private static final int MAX_BUCKET_VOLUME = 1000_0000;

    Map<K, AtomicReference<Set<T>>> serviceMaps = new HashMap<>();
    Map<K, AtomicReference<AtomicInteger>> serviceCount = new HashMap<>();
    private AtomicBoolean isCleaning = new AtomicBoolean(false);
    @Override
    public boolean isRepeatedOperation(K serviceName, T uuid) {
        return serviceMaps.get(serviceName) != null && serviceMaps.get(serviceName).get().contains(uuid);
    }

    private synchronized void initService(K serviceName) {
        if (serviceMaps.containsKey(serviceName)) {
            return;
        }
        serviceMaps.put(serviceName, new AtomicReference<>(ConcurrentHashMap.newKeySet()));
        serviceCount.put(serviceName, new AtomicReference<>(new AtomicInteger(0)));
    }

    @Override
    public void release(K serviceName, T uuid) {
        if (serviceMaps.containsKey(serviceName)) {
            serviceMaps.get(serviceName).get().remove(uuid);
        }
    }

    @Override
    public void set(K serviceName, T value) throws IllegalStateException {
        if (!serviceMaps.containsKey(serviceName)) {
            initService(serviceName);
        }
        serviceCount.get(serviceName).get().incrementAndGet();
        serviceMaps.get(serviceName).get().add(value);
        if (!serviceMaps.get(serviceName).get().contains(value)) {
            throw new IllegalStateException("set operation failed, the current cleanup jobs might have replaced the previous data");
        }
    }

    public synchronized void cleanup() {
        if (isCleaning.get()) {
            return;
        }
        isCleaning.set(true);
        for (K key : serviceMaps.keySet()) {
            AtomicInteger atoi = serviceCount.get(key).get();
            Set<T> vals = serviceMaps.get(key).get();
            if (atoi.get() > MAX_BUCKET_VOLUME) {
                serviceCount.get(key).compareAndSet(atoi, new AtomicInteger(0));
                serviceMaps.get(key).compareAndSet(vals, ConcurrentHashMap.newKeySet());
            }
        }
        isCleaning.set(false);
    }
}
