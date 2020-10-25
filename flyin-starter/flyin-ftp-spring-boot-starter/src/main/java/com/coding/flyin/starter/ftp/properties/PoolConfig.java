package com.coding.flyin.starter.ftp.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "flyin.ftp.pool")
public class PoolConfig extends GenericObjectPoolConfig<FTPClient> {

    /** 最大连接数：默认 8个. */
    private int maxTotal = DEFAULT_MAX_TOTAL;

    /** 最大空闲连接数：默认 8个. */
    private int maxIdle = DEFAULT_MAX_IDLE;

    /** 最小空闲连接数：默认 0个. */
    private int minIdle = DEFAULT_MIN_IDLE;

    /** 连接的最小空闲时间，达到此值后空闲连接将被移除：默认 1000L * 60L * 30L毫秒=30分钟. */
    private long minEvictableIdleTimeMillis = DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

    /** 做空闲连接检测时，每次的采样数：默认 3. */
    private int numTestsPerEvictionRun = DEFAULT_NUM_TESTS_PER_EVICTION_RUN;

    private boolean testOnCreate = DEFAULT_TEST_ON_CREATE;

    /** 向连接池借用连接时是否做连接有效性检测（ping），无效连接会被移除，每次借用多执行一次ping命令：默认 false. */
    private boolean testOnBorrow = DEFAULT_TEST_ON_BORROW;

    /** 向连接池归还连接时是否做连接有效性检测（ping），无效连接会被移除，每次归还多执行一次ping命令：默认 false. */
    private boolean testOnReturn = DEFAULT_TEST_ON_RETURN;

    /** 向连接池借用连接时是否做连接空闲检测，空闲超时的连接会被移除：默认 false. */
    private boolean testWhileIdle = DEFAULT_TEST_WHILE_IDLE;

    /** 空闲连接的检测周期（单位：毫秒）：默认 -1，表示不做检测. */
    private long timeBetweenEvictionRunsMillis = DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;

    /** 当连接池用尽后，调用者是否要等待，这个参数是和 maxWaitMillis 对应的，只有当此参数为true时，maxWaitMillis才生效：默认 true. */
    private boolean blockWhenExhausted = DEFAULT_BLOCK_WHEN_EXHAUSTED;

    /** 【原始默认-1永不超时】当连接池资源用尽后，调用者的最大等待时间（单位：毫秒）：默认值30000. */
    private long maxWaitMillis = 30000L;

    /**
     * 是否开启jmx监控，如果应用开启了jmx端口并且jmxEnabled设置为true，<br>
     * 就可以通过jconsole或者jvisualvm看到关于连接池的相关统计，有助于了解连接池的使用情况，并且可以针对其他做监控统计。<br>
     * 默认： true.
     */
    private boolean jmxEnabled = DEFAULT_JMX_ENABLE;
}
