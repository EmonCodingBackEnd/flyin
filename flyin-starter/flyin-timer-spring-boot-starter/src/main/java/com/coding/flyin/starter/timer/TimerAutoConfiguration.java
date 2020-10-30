package com.coding.flyin.starter.timer;

import com.coding.flyin.starter.timer.pool.AsyncPoolConfiguration;
import com.coding.flyin.starter.timer.pool.DelayPoolConfiguration;
import com.coding.flyin.starter.timer.pool.SchedulePoolConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;

@Configuration
@ConditionalOnClass({RedissonClient.class, SchedulingConfigurer.class, AsyncConfigurer.class})
@ConditionalOnProperty(prefix = "flyin.timer", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(PooledTimerTaskProperties.class)
@Import({
    DelayPoolConfiguration.class,
    SchedulePoolConfiguration.class,
    AsyncPoolConfiguration.class
})
@Slf4j
public class TimerAutoConfiguration {}
