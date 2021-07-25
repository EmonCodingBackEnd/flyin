package com.coding.flyin.starter.timer.delay.queue;

import com.coding.flyin.starter.timer.PooledTimerTaskProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public final class DefaultSingleDelayedQueue extends AbstractSingleDelayedQueue {
    public DefaultSingleDelayedQueue(
            PooledTimerTaskProperties.Delay delay, ThreadPoolTaskExecutor delayPoolQueueExecutor) {
        super(delay, delayPoolQueueExecutor);
    }
}
