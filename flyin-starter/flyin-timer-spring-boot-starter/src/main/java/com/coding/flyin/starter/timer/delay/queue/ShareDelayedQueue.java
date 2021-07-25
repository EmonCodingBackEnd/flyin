package com.coding.flyin.starter.timer.delay.queue;

import com.coding.flyin.starter.timer.delay.DelayTask;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;

/** {@linkplain DelayedQueue 参见接口定义 DelayedQueue} */
public final class ShareDelayedQueue {

    private static AbstractShareDelayedQueue defaultShareDelayedQueue;

    public ShareDelayedQueue(AbstractShareDelayedQueue defaultShareDelayedQueue) {
        ShareDelayedQueue.defaultShareDelayedQueue = defaultShareDelayedQueue;
    }

    /** {@linkplain DelayedQueue#size() 参见接口定义 DelayedQueue#size() }. */
    public static Integer size() {
        return defaultShareDelayedQueue.size();
    }

    /** {@linkplain DelayedQueue#size(Class) 参见接口定义 DelayedQueue#size(Class) }. */
    public static <T extends DelayTask> Integer size(@NonNull Class<T> clazz) {
        return defaultShareDelayedQueue.size(clazz);
    }

    /** {@linkplain DelayedQueue#exists(DelayTask) 参见接口定义 DelayedQueue#exists(DelayTask) }. */
    public static boolean exists(@NonNull DelayTask delayTask) {
        return defaultShareDelayedQueue.exists(delayTask);
    }

    /**
     * {@linkplain DelayedQueue#put(DelayTask, long, TimeUnit) 参见接口定义 @linkplain
     * DelayedQueue#put(DelayTask, long, TimeUnit) }.
     */
    public static void put(@NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        defaultShareDelayedQueue.put(delayTask, timeout, timeUnit);
    }

    /**
     * {@linkplain DelayedQueue#putIfAbsent(DelayTask, long, TimeUnit) 参见接口定义
     * DelayedQueue#putIfAbsent(DelayTask, long, TimeUnit) }.
     */
    public static Boolean putIfAbsent(
            @NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        return defaultShareDelayedQueue.putIfAbsent(delayTask, timeout, timeUnit);
    }

    /** {@linkplain DelayedQueue#remove(DelayTask) 参见接口定义 DelayedQueue#remove(DelayTask) }. */
    public static Boolean remove(@NonNull DelayTask delayTask) {
        return defaultShareDelayedQueue.remove(delayTask);
    }

    /**
     * {@linkplain DelayedQueue#removeUntilNone(DelayTask) 参见接口定义
     * DelayedQueue#removeUntilNone(DelayTask) }.
     */
    public static Integer removeUntilNone(DelayTask delayTask) {
        return defaultShareDelayedQueue.removeUntilNone(delayTask);
    }
}
