package com.coding.flyin.starter.timer.delay.queue;

import com.coding.flyin.starter.timer.delay.DelayTask;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/** {@linkplain DelayedQueue 参见接口定义 DelayedQueue} */
@Slf4j
public final class SingleDelayedQueue {

    private static AbstractSingleDelayedQueue defaultSingleDelayedQueue;

    public SingleDelayedQueue(AbstractSingleDelayedQueue defaultSingleDelayedQueue) {
        SingleDelayedQueue.defaultSingleDelayedQueue = defaultSingleDelayedQueue;
    }

    /** {@linkplain DelayedQueue#size() 参见接口定义 DelayedQueue#size() }. */
    public static Integer size() {
        return defaultSingleDelayedQueue.size();
    }

    /** {@linkplain DelayedQueue#size(Class) 参见接口定义 DelayedQueue#size(Class) }. */
    public static <T extends DelayTask> Integer size(@NonNull Class<T> clazz) {
        return defaultSingleDelayedQueue.size(clazz);
    }

    /** {@linkplain DelayedQueue#exists(DelayTask) 参见接口定义 DelayedQueue#exists(DelayTask) }. */
    public static boolean exists(@NonNull DelayTask delayTask) {
        return defaultSingleDelayedQueue.exists(delayTask);
    }

    /**
     * {@linkplain DelayedQueue#put(DelayTask, long, TimeUnit) 参见接口定义 @linkplain
     * DelayedQueue#put(DelayTask, long, TimeUnit) }.
     */
    public static void put(@NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        defaultSingleDelayedQueue.put(delayTask, timeout, timeUnit);
    }

    /**
     * {@linkplain DelayedQueue#putIfAbsent(DelayTask, long, TimeUnit) 参见接口定义
     * DelayedQueue#putIfAbsent(DelayTask, long, TimeUnit) }.
     */
    public static Boolean putIfAbsent(
            @NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        return defaultSingleDelayedQueue.putIfAbsent(delayTask, timeout, timeUnit);
    }

    /** {@linkplain DelayedQueue#remove(DelayTask) 参见接口定义 DelayedQueue#remove(DelayTask) }. */
    public static Boolean remove(@NonNull DelayTask delayTask) {
        return defaultSingleDelayedQueue.remove(delayTask);
    }

    /**
     * {@linkplain DelayedQueue#removeUntilNone(DelayTask) 参见接口定义
     * DelayedQueue#removeUntilNone(DelayTask) }.
     */
    public static Integer removeUntilNone(@NonNull DelayTask delayTask) {
        return defaultSingleDelayedQueue.removeUntilNone(delayTask);
    }
}
