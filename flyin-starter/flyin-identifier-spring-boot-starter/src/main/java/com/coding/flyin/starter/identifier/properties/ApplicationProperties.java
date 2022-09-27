package com.coding.flyin.starter.identifier.properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.coding.flyin.starter.identifier.common.SerializeStrategy;
import com.coding.flyin.starter.identifier.register.MachineRegister;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Application 配置
 */
@ConfigurationProperties(prefix = "flyin.identifier")
@Data
@Slf4j
public class ApplicationProperties {

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * 分组名字
     */
    private String group;

    /**
     * 生成策略，默认snowflake
     */
    private String strategy = "snowflake";

    /**
     * zk节点信息本地缓存文件路径，默认 .tmp/flyin/identifier/${group}.cache
     */
    private String registryFile;

    /**
     * zk节点是否持久化存储，默认 false
     */
    private boolean durable;

    /**
     * 序列化工具，默认 jackson
     */
    private String serialize = SerializeStrategy.SERIALIZE_JSON_JACKSON;

    /**
     * 是否使用本地缓存（如果不依赖本地缓存，那么每次都会申请一个新的workerId）
     * <p>
     * 需要注意的是，如果不依赖本地缓存，且开启了节点持久化存储。会在一定次数以后耗尽可用的1024（0-1023）个节点信息。
     */
    private boolean cacheable = true;

    /**
     * 应用端口，可有效降低节点被误删的可能
     */
    @Value("${server.port}")
    private Integer port;

    /**
     * 节点上报频率，默认5s
     */
    private int nodeUploadIntervalSeconds = 5;

    public void setNodeUploadIntervalSeconds(int nodeUploadIntervalSeconds) {
        if (nodeUploadIntervalSeconds > 0) {
            this.nodeUploadIntervalSeconds = nodeUploadIntervalSeconds;
        }
    }

    private DurableExtend durableExtend = new DurableExtend();

    @PostConstruct
    public void init() {
        if (durable) {
            if (durableExtend.getNodeWarnThreshold() > durableExtend.getNodeCleanThreshold()) {
                log.warn("【Identifier】nodeWarnThreshold and nodeCleanThreshold is invalid reset to default");
                durableExtend.setNodeWarnThreshold((int)(MachineRegister.MAX_MACHINE_NUM / 2));
                durableExtend.setNodeCleanThreshold((int)(MachineRegister.MAX_MACHINE_NUM / 4) * 3);
            }
        }
    }

    /**
     * 节点持久化时的扩展属性
     */
    @Data
    public static class DurableExtend {

        /**
         * 节点持久化时，持久化节点的空闲时间，超过该时间被认为是无效节点，默认3600*24=86400秒
         */
        private int nodeIdleToInvalidSeconds = 3600 * 24;

        public void setNodeIdleToInvalidSeconds(int nodeIdleToInvalidSeconds) {
            if (nodeIdleToInvalidSeconds > 0) {
                this.nodeIdleToInvalidSeconds = nodeIdleToInvalidSeconds;
            }
        }

        /**
         * 节点持久化时，持久化节点触发警告的阈值，大于0小于${nodeCleanThreshold}
         */
        private int nodeWarnThreshold = (int)(MachineRegister.MAX_MACHINE_NUM / 2);

        public void setNodeWarnThreshold(int nodeWarnThreshold) {
            if (nodeWarnThreshold > 0) {
                this.nodeWarnThreshold = nodeWarnThreshold;
            }
        }

        /**
         * 节点持久化时，持久化节点触发清理的阈值，大于0小于1024
         */
        private int nodeCleanThreshold = (int)(MachineRegister.MAX_MACHINE_NUM / 4) * 3;

        public void setNodeCleanThreshold(int nodeCleanThreshold) {
            if (nodeCleanThreshold > 0) {
                this.nodeCleanThreshold = nodeCleanThreshold;
            }
        }

        /**
         * 无效节点清理频率，默认600秒
         */
        private int nodeCleanIntervalSeconds = 600;

        public void setNodeCleanIntervalSeconds(int nodeCleanIntervalSeconds) {
            if (nodeCleanIntervalSeconds > 0) {
                this.nodeCleanIntervalSeconds = nodeCleanIntervalSeconds;
            }
        }

        /**
         * 对无效节点清理时，测试连通性的超时时间，默认3s
         */
        private int connectionTimeoutMs = 3 * 1000;

        public void setConnectionTimeoutMs(int connectionTimeoutMs) {
            if (connectionTimeoutMs > 0) {
                this.connectionTimeoutMs = connectionTimeoutMs;
            }
        }
    }

}
