package com.coding.flyin.starter.timer.pool;

import com.coding.flyin.starter.timer.PooledTimerTaskProperties;
import com.coding.flyin.starter.timer.async.AsyncConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@EnableAsync
@ConditionalOnProperty(prefix = "flyin.timer.async", name = "enabled", havingValue = "true")
public class AsyncPoolConfiguration {

    @Autowired private PooledTimerTaskProperties pooledTimerTaskProperties;

    /**
     * 异步任务使用策略，多线程处理。
     *
     * @return -
     */
    @Bean
    public Executor asyncPoolExecutor() {
        PooledTimerTaskProperties.Async async = pooledTimerTaskProperties.getAsync();
        log.info(
                "【异步任务线程池配置】threadNamePrefix={},corePoolSize={},maxPoolSize={},queueCapacity={},keeyAliveSecond={}",
                async.getThreadNamePrefix(),
                async.getCorePoolSize(),
                async.getMaxPoolSize(),
                async.getQueueCapacity(),
                async.getKeeyAliveSecond());
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(async.getThreadNamePrefix());
        executor.setCorePoolSize(async.getCorePoolSize());
        executor.setMaxPoolSize(async.getMaxPoolSize());
        executor.setQueueCapacity(async.getQueueCapacity());
        executor.setKeepAliveSeconds(async.getKeeyAliveSecond());

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(async.getAwaitTerminationSeconds());

        /*
         * Rejected-policy：当pool已经达到max size的时候，如何处理新任务。
         * CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean({AsyncConfig.class})
    public AsyncConfig asyncConfig(@Qualifier("asyncPoolExecutor") Executor asyncPoolExecutor) {
        // [lm's ps]: 20201030 16:59 禁止 asyncPoolExecutor() 这种方式调用 @Bean 定义的实例，会导致重复生成实例
        // return new AsyncConfig(asyncPoolExecutor());
        return new AsyncConfig(asyncPoolExecutor);
    }
}
