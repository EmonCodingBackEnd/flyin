package com.coding.flyin.starter.identifier.generator;

import java.io.IOException;
import java.util.Arrays;

import com.coding.flyin.starter.identifier.algorithm.Snowflake;
import com.coding.flyin.starter.identifier.exception.RegistryException;
import com.coding.flyin.starter.identifier.register.GeneratorConnector;
import com.coding.flyin.starter.identifier.register.zookeeper.NodeInfo;
import com.coding.flyin.starter.identifier.register.zookeeper.ZookeeperMachineRegister;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SnowflakeGenerator implements IdentifierGenerator<Long>, GeneratorConnector {

    /**
     * Snowflake算法实现
     */
    private Snowflake snowflake;

    /**
     * Snowflake注册 注册
     */
    private final ZookeeperMachineRegister register;

    /**
     * 是否已初始化
     */
    private volatile boolean initialized = false;

    /**
     * 当前是否正常运行
     */
    private volatile boolean working = false;

    /**
     * 是否正在连接到注册中心
     */
    private volatile boolean connecting = false;

    public SnowflakeGenerator(ZookeeperMachineRegister register) {
        this.register = register;
    }

    @Override
    public Long nextId() {
        if (isWorking()) {
            return snowflake.nextId();
        }
        throw new IllegalStateException("worker isn't working, registry center may shutdown");
    }

    @Override
    public Long[] nextId(int size) {
        if (isWorking()) {
            return Arrays.stream(snowflake.nextId(size)).boxed().toArray(Long[]::new);
        }
        throw new IllegalStateException("worker isn't working, registry center may shutdown");
    }

    @Override
    public void init() {
        if (!initialized) {
            connect();
            initialized = true;
        }
    }

    @Override
    public void connect() {
        if (!isConnecting()) {
            working = false;
            connecting = true;

            NodeInfo nodeInfo = register.register();
            long dataCenterId = nodeInfo.getDataCenterId();
            long workerId = nodeInfo.getWorkerId();
            log.info("【Identifier】register success, nodeInfo={}", nodeInfo);
            if (dataCenterId >= 0 && workerId >= 0) {
                snowflake = Snowflake.createInstance(dataCenterId, workerId);
                working = true;
                connecting = false;
            } else {
                throw new RegistryException("failed to get worker id");
            }
        } else {
            log.info("worker is connecting, skip this time of register.");
        }
    }

    @Override
    public boolean isWorking() {
        return this.working;
    }

    @Override
    public boolean isConnecting() {
        return this.connecting;
    }

    @Override
    public void close() throws IOException {
        // 关闭，先重置状态(避免非持久化状态下，ZK删除 machineId，其他机器抢注，会导致 machineId 重新生成的BUG)
        reset();
        register.logout();
    }

    /**
     * 重置连接状态
     */
    protected void reset() {
        initialized = false;
        working = false;
        connecting = false;
    }
}
