package com.coding.flyin.starter.timer.pool;

import com.coding.flyin.starter.timer.PooledTimerTaskProperties;
import com.coding.flyin.starter.timer.delay.queue.DefaultShareDelayedQueue;
import com.coding.flyin.starter.timer.delay.queue.DefaultSingleDelayedQueue;
import com.coding.flyin.starter.timer.delay.queue.ShareDelayedQueue;
import com.coding.flyin.starter.timer.delay.queue.SingleDelayedQueue;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@ConditionalOnProperty(prefix = "flyin.timer.delay", name = "enabled", havingValue = "true")
public class DelayPoolConfiguration {

    @Autowired private PooledTimerTaskProperties pooledTimerTaskProperties;

    @Autowired private RedissonClient redissonClient;

    //    @Bean
    private ThreadPoolTaskExecutor delayPoolQueueExecutor() {
        PooledTimerTaskProperties.Delay delay = pooledTimerTaskProperties.getDelay();
        log.info(
                "【延时任务线程池配置】threadNamePrefix={},corePoolSize={},maxPoolSize={},queueCapacity={},keeyAliveSecond={}",
                delay.getThreadNamePrefix(),
                delay.getCorePoolSize(),
                delay.getMaxPoolSize(),
                delay.getQueueCapacity(),
                delay.getKeeyAliveSecond());
        ThreadPoolTaskExecutor delayQueueExecutor;
        delayQueueExecutor = new ThreadPoolTaskExecutor();
        delayQueueExecutor.setThreadNamePrefix(delay.getThreadNamePrefix());
        delayQueueExecutor.setCorePoolSize(delay.getCorePoolSize());
        delayQueueExecutor.setMaxPoolSize(delay.getMaxPoolSize());
        delayQueueExecutor.setQueueCapacity(delay.getQueueCapacity());
        delayQueueExecutor.setKeepAliveSeconds(delay.getKeeyAliveSecond());

        delayQueueExecutor.setWaitForTasksToCompleteOnShutdown(true);
        delayQueueExecutor.setAwaitTerminationSeconds(delay.getAwaitTerminationSeconds());

        /*
         * Rejected-policy：当pool已经达到max size的时候，如何处理新任务。
         * CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行。
         */
        delayQueueExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        delayQueueExecutor.initialize();
        return delayQueueExecutor;
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "flyin.timer.delay",
            name = "standalone",
            havingValue = "true",
            matchIfMissing = true)
    public SingleDelayedQueue singleDelayedQueue(
            /*@Qualifier("delayPoolQueueExecutor") ThreadPoolTaskExecutor delayPoolQueueExecutor*/ ) {
        ThreadPoolTaskExecutor delayPoolQueueExecutor = delayPoolQueueExecutor();
        DefaultSingleDelayedQueue delayedQueue =
                new DefaultSingleDelayedQueue(
                        pooledTimerTaskProperties.getDelay(), delayPoolQueueExecutor);

        //noinspection InstantiationOfUtilityClass
        return new SingleDelayedQueue(delayedQueue);
    }

    @Bean
    @ConditionalOnMissingBean({SingleDelayedQueue.class})
    public ShareDelayedQueue shareDelayedQueue(
            /*@Qualifier("delayPoolQueueExecutor") ThreadPoolTaskExecutor delayPoolQueueExecutor*/ ) {
        ThreadPoolTaskExecutor delayPoolQueueExecutor = delayPoolQueueExecutor();
        DefaultShareDelayedQueue delayedQueue =
                new DefaultShareDelayedQueue(
                        pooledTimerTaskProperties.getDelay(),
                        delayPoolQueueExecutor,
                        redissonClient);

        //noinspection InstantiationOfUtilityClass
        return new ShareDelayedQueue(delayedQueue);
    }
}
