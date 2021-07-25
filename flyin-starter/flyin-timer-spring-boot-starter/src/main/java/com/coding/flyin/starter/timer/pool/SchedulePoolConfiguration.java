package com.coding.flyin.starter.timer.pool;

import com.coding.flyin.starter.timer.PooledTimerTaskProperties;
import com.coding.flyin.starter.timer.schedule.ScheduleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Slf4j
@EnableScheduling
@ConditionalOnProperty(prefix = "flyin.timer.schedule", name = "enabled", havingValue = "true")
public class SchedulePoolConfiguration {

    @Autowired private PooledTimerTaskProperties pooledTimerTaskProperties;

    /**
     * 并行任务使用策略，多线程处理。
     *
     * @return -
     */
//    @Bean
    public Executor schedulePoolExecutor() {
        PooledTimerTaskProperties.Schedule schedule = pooledTimerTaskProperties.getSchedule();
        log.info(
                "【定时器线程池配置】threadNamePrefix={},poolSize={},awaitTerminationSeconds={}",
                schedule.getThreadNamePrefix(),
                schedule.getPoolSize(),
                schedule.getAwaitTerminationSeconds());
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix(schedule.getThreadNamePrefix());
        scheduler.setPoolSize(schedule.getPoolSize());

        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(schedule.getAwaitTerminationSeconds());

        return scheduler;
    }

    @Bean
    @ConditionalOnMissingBean({ScheduleConfig.class})
    public ScheduleConfig scheduleConfig(
            /*@Qualifier("schedulePoolExecutor") Executor schedulePoolExecutor*/) {
        Executor schedulePoolExecutor = schedulePoolExecutor();
        return new ScheduleConfig(schedulePoolExecutor);
    }
}
