package com.coding.flyin.starter.identifier.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Zookeeper 配置
 */
@ConfigurationProperties(prefix = "flyin.identifier.zookeeper")
@Data
@Slf4j
public class ZookeeperProperties {

    /**
     * 连接Zookeeper服务器的列表. 包括IP地址和端口号. 多个地址用逗号分隔. 如: host1:2181,host2:2181
     */
    private String connectString;

    /**
     * 命名空间.
     */
    private String namespace = "flyin/identifier";

    /**
     * 等待重试的间隔时间的初始值. 单位毫秒. 默认1s
     */
    private int baseSleepTimeMs = 1000;

    /**
     * 等待重试的间隔时间的最大值. 单位毫秒. 默认3s
     */
    private int maxSleepMs = 3000;

    /**
     * 最大重试次数.
     */
    private int maxRetries = 3;

    /**
     * 会话超时时间. 单位毫秒. 默认60s
     */
    private int sessionTimeoutMs;

    /**
     * 连接超时时间. 单位毫秒. 默认15s
     */
    private int connectionTimeoutMs;

    /**
     * 连接Zookeeper的权限令牌. 缺省为不需要权限验证.
     */
    private String digest;

}
