package com.coding.flyin.starter.timer.delay.queue;

import com.coding.flyin.starter.timer.PooledTimerTaskProperties;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public final class DefaultShareDelayedQueue extends AbstractShareDelayedQueue {
    public DefaultShareDelayedQueue(
            PooledTimerTaskProperties.Delay delay,
            ThreadPoolTaskExecutor delayPoolQueueExecutor,
            RedissonClient redissonClient) {
        super(delay, delayPoolQueueExecutor, redissonClient);
    }
}
