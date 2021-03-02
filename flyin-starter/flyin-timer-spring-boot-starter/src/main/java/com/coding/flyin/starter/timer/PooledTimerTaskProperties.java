package com.coding.flyin.starter.timer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@Data
@ConfigurationProperties(prefix = "flyin.timer")
@Slf4j
public class PooledTimerTaskProperties {

    private boolean enabled = false;

    private Delay delay = new Delay();

    private Schedule schedule = new Schedule();

    private Async async = new Async();

    @PostConstruct
    public void init() {
        log.info("【timer任务池】配置启动 {}", this);
    }

    @Override
    public String toString() {
        return "PooledTimerTaskProperties{"
                + "enabled="
                + enabled
                + ", \ndelay="
                + delay
                + ", \nschedule="
                + schedule
                + ", \nasync="
                + async
                + '}';
    }

    @Data
    public static class Delay {

        private boolean enabled = false;

        /** 是否单机模式（默认：true）. */
        private boolean standalone = true;

        /** 延迟队列在redis中存储的key. */
        private String delayTaskQueueRedisKey = "flyin:delayQueue";

        /** 执行任务队列的线程名称前缀. */
        private String threadNamePrefix = "TIMER_DELAY-";

        /** 核心线程数. */
        private int corePoolSize = 8;

        /** 最大线程数. */
        private int maxPoolSize = 10;

        /** 队列最大长度. */
        private int queueCapacity = 100;

        /** 线程池维护线程所允许的空闲时间，默认为60s. */
        private int keeyAliveSecond = 60;

        /** 等待任务在关机时完成--表明等待所有线程执行完，等待时间（默认为0，此时立即停止）. */
        private int awaitTerminationSeconds = 60;

        private String delayTaskQueueDaemonThreadName = "DelayTaskQueueDaemonThread";
    }

    @Data
    public static class Schedule {

        private boolean enabled = false;

        /** 执行任务队列的线程名称前缀. */
        private String threadNamePrefix = "TIMER_SCHEDULE-";

        private int poolSize = 10;

        /** 等待任务在关机时完成--表明等待所有线程执行完，等待时间（默认为0，此时立即停止）. */
        private int awaitTerminationSeconds = 60;
    }

    @Data
    public static class Async {

        private boolean enabled = false;

        /** 执行任务队列的线程名称前缀. */
        private String threadNamePrefix = "TIMER_ASYNC-";

        /** 核心线程数. */
        private int corePoolSize = 8;

        /** 最大线程数. */
        private int maxPoolSize = 10;

        /** 队列最大长度. */
        private int queueCapacity = 100;

        /** 线程池维护线程所允许的空闲时间，默认为60s. */
        private int keeyAliveSecond = 60;

        /** 等待任务在关机时完成--表明等待所有线程执行完，等待时间（默认为0，此时立即停止）. */
        private int awaitTerminationSeconds = 60;
    }
}
