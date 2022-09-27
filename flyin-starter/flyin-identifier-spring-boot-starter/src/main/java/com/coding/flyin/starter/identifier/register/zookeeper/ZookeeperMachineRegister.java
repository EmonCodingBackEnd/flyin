package com.coding.flyin.starter.identifier.register.zookeeper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.util.StringUtils;

import com.coding.flyin.starter.identifier.common.SerializeStrategy;
import com.coding.flyin.starter.identifier.exception.ConfigException;
import com.coding.flyin.starter.identifier.exception.RegistryException;
import com.coding.flyin.starter.identifier.properties.ApplicationProperties;
import com.coding.flyin.starter.identifier.register.AbstractMachineRegister;
import com.coding.flyin.starter.identifier.registry.RegistryCenter;
import com.coding.flyin.starter.identifier.serialize.json.FastJsonSerializer;
import com.coding.flyin.starter.identifier.serialize.json.JacksonSerializer;

import lombok.extern.slf4j.Slf4j;

/**
 * 机器信息注册
 */
@Slf4j
public class ZookeeperMachineRegister extends AbstractMachineRegister {

    public ZookeeperMachineRegister(RegistryCenter registryCenter, ApplicationProperties applicationProperties) {
        this.registryCenter = registryCenter;
        this.applicationProperties = applicationProperties;

        this.nodePath = new NodePath(applicationProperties.getGroup());

        if (StringUtils.isEmpty(applicationProperties.getGroup())) {
            throw new ConfigException("flyin.identifier.group must be not null", applicationProperties.getGroup());
        }

        if (SerializeStrategy.SERIALIZE_JSON_FASTJSON.equals(applicationProperties.getSerialize())) {
            this.jsonSerializer = new FastJsonSerializer<>();
        } else if (SerializeStrategy.SERIALIZE_JSON_JACKSON.equals(applicationProperties.getSerialize())) {
            this.jsonSerializer = new JacksonSerializer<>();
        } else {
            throw new ConfigException("unsupported serialize strategy: %s, use: [fastjson / jackson]",
                applicationProperties.getSerialize());
        }

        if (StringUtils.isEmpty(applicationProperties.getRegistryFile())) {
            applicationProperties.setRegistryFile(getDefaultFilePath(nodePath.getGroupName()));
        }

    }

    /**
     * 向zookeeper注册workerId
     * 
     * @return workerId workerId
     */
    @Override
    public NodeInfo register() {
        InterProcessMutex lock = null;
        try {
            CuratorFramework client = (CuratorFramework)registryCenter.getRawClient();
            lock = new InterProcessMutex(client, nodePath.getGroupPath());
            int numOfChildren = registryCenter.getNumChildren(nodePath.getMachinePath());
            if (numOfChildren < MAX_MACHINE_NUM) {
                if (!lock.acquire(MAX_LOCK_WAIT_TIME_MS, TimeUnit.MILLISECONDS)) {
                    String message = String.format("acquire lock failed after %s ms.", MAX_LOCK_WAIT_TIME_MS);
                    throw new TimeoutException(message);
                }
                NodeInfo localNodeInfo = getLocalNodeInfo();
                List<String> children = registryCenter.getChildrenKeys(nodePath.getMachinePath());
                if (children.size() >= applicationProperties.getDurableExtend().getNodeWarnThreshold()) {
                    log.warn(
                        "warn machine num reached, please note whether the machine node cannot be recycled! max_machine_num={}, machine_num={}, warn_machine_num={}",
                        MAX_MACHINE_NUM, children.size(),
                        applicationProperties.getDurableExtend().getNodeWarnThreshold());
                }

                /*
                 * 有本地缓存的节点信息，同时ZK也有这条数据；判断是否依赖本地缓存，如果不依赖本地缓存，则每次都会申请新的id
                 */
                if (applicationProperties.isCacheable() && localNodeInfo != null
                    && children.contains(String.valueOf(localNodeInfo.getMachineId()))) {
                    String key = getNodePathKey(nodePath, localNodeInfo.getMachineId());
                    String zkNodeInfoJson = registryCenter.get(key);
                    NodeInfo zkNodeInfo = createNodeInfoFromJsonStr(zkNodeInfoJson);
                    if (checkNodeInfo(localNodeInfo, zkNodeInfo)) {
                        // 更新ZK节点信息，保存本地缓存，开启定时上报任务
                        nodePath.setMachineId(zkNodeInfo.getMachineId());
                        zkNodeInfo.setUpdateTime(new Date());
                        updateZookeeperNodeInfo(key, zkNodeInfo);
                        saveLocalNodeInfo(zkNodeInfo);
                        executeUploadNodeInfoTask(key, zkNodeInfo);
                        return zkNodeInfo;
                    }
                }
                // 无本地信息或者缓存数据不匹配，开始向ZK申请节点机器ID
                for (int workerId = 0; workerId < MAX_MACHINE_NUM; workerId++) {
                    String workerIdStr = String.valueOf(workerId);
                    if (!children.contains(workerIdStr)) { // 申请成功
                        NodeInfo applyNodeInfo = createNodeInfo(nodePath.getGroupName(), workerId);
                        nodePath.setMachineId(applyNodeInfo.getMachineId());
                        // 保存ZK节点信息，保存本地缓存，开启定时上报任务
                        saveZookeeperNodeInfo(nodePath.getMachineIdPath(), applyNodeInfo);
                        saveLocalNodeInfo(applyNodeInfo);
                        executeUploadNodeInfoTask(nodePath.getMachineIdPath(), applyNodeInfo);
                        return applyNodeInfo;
                    }
                }
            }
            throw new RegistryException("max machine num reached. register failed");
        } catch (RegistryException e) {
            throw e;
        } catch (Exception e) {
            log.error("", e);
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            try {
                if (lock != null) {
                    lock.release();
                }
            } catch (Exception ex) {
                log.error("", ex);
            }

            // 自动清除失效节点
            this.clearExpiredNodes();
        }
    }

    /**
     * 关闭注册
     */
    @Override
    public synchronized void logout() {
        CuratorFramework client = (CuratorFramework)registryCenter.getRawClient();
        if (client != null && client.getState() == CuratorFrameworkState.STARTED) {
            // 移除注册节点（最大程度的自动释放资源）
            registryCenter.remove(nodePath.getMachineIdPath());
            // 关闭连接
            registryCenter.close();
        }
    }

    /**
     * 更新节点信息Task
     * 
     * @param key zk path
     * @param nodeInfo 节点信息
     */
    private void executeUploadNodeInfoTask(final String key, final NodeInfo nodeInfo) {
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "upload node info task thread");
            thread.setDaemon(true);
            return thread;
        }).scheduleWithFixedDelay(() -> updateZookeeperNodeInfo(key, nodeInfo), 3L,
            applicationProperties.getNodeUploadIntervalSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 保存ZK节点信息
     * 
     * @param key
     * @param nodeInfo
     */
    private void saveZookeeperNodeInfo(String key, NodeInfo nodeInfo) throws Exception {
        if (applicationProperties.isDurable()) {
            registryCenter.persist(key, jsonizeNodeInfo(nodeInfo));
        } else {
            registryCenter.persistEphemeral(key, jsonizeNodeInfo(nodeInfo));
        }
    }

    /**
     * 刷新ZK节点信息（修改updateTime）
     * 
     * @param key
     * @param nodeInfo
     */
    private void updateZookeeperNodeInfo(String key, NodeInfo nodeInfo) {
        try {
            nodeInfo.setUpdateTime(new Date());
            if (applicationProperties.isDurable()) {
                registryCenter.persist(key, jsonizeNodeInfo(nodeInfo));
            } else {
                registryCenter.persistEphemeral(key, jsonizeNodeInfo(nodeInfo));
            }
        } catch (Exception e) {
            log.error("update zookeeper node info error", e);
        }
    }

    /**
     * 异步清理失效节点
     */
    public void clearExpiredNodes() {
        ApplicationProperties.DurableExtend durableExtend = applicationProperties.getDurableExtend();
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "nodeCleanThread");
            thread.setDaemon(true);
            return thread;
        }).scheduleWithFixedDelay(() -> {
            InterProcessMutex mutex = null;
            try {
                CuratorFramework client = (CuratorFramework)registryCenter.getRawClient();
                mutex = new InterProcessMutex(client, nodePath.getGroupPath());
                if (mutex.acquire(MAX_LOCK_WAIT_TIME_MS, TimeUnit.SECONDS)) {
                    List<String> children = registryCenter.getChildrenKeys(nodePath.getMachinePath());
                    if (children.size() >= durableExtend.getNodeCleanThreshold()) {
                        log.warn(
                            "clean machine num reached, trigger auto cleanup! max_machine_num={}, machine_num={}, clean_machine_num={}",
                            MAX_MACHINE_NUM, children.size(), durableExtend.getNodeCleanThreshold());
                        for (String machineId : children) {
                            try {
                                String key = nodePath.getMachinePath() + "/" + machineId;
                                String value = registryCenter.getDirectly(key);

                                NodeInfo nodeInfo = createNodeInfoFromJsonStr(value);
                                long updateTime = nodeInfo.getUpdateTime().getTime();
                                long nodeIdleToInvalidTime = durableExtend.getNodeIdleToInvalidSeconds() * 1000L;
                                nodeInfo.getUpdateTime().setTime(updateTime + nodeIdleToInvalidTime);

                                // 如果已失效
                                if (nodeInfo.getUpdateTime().before(new Date())) {
                                    removeNode(nodeInfo, key);
                                }
                            } catch (Exception e) {
                                log.error(String.format("invalid node auto cleanup error, machineId=%s", machineId), e);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("invalid node auto cleanup error", e);
            } finally {
                try {
                    if (mutex != null) {
                        mutex.release();
                    }
                } catch (Exception e) {
                    log.error("invalid node auto cleanup lock release error!", e);
                }
            }
        }, 3L, durableExtend.getNodeCleanIntervalSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 删除节点
     * 
     * @param nodeInfo - 节点信息
     * @param path - 节点路径
     */
    private void removeNode(NodeInfo nodeInfo, String path) {
        String ip = nodeInfo.getIp();
        Integer port = nodeInfo.getPort();
        Integer machineId = nodeInfo.getMachineId();

        String target = String.format("clean node %s:%d-%s", ip, port, machineId);
        ApplicationProperties.DurableExtend durableExtend = applicationProperties.getDurableExtend();
        if (port != null) {
            try (Socket socket = new Socket()) {
                // 连通性检测
                log.info("{} --> start connecting...", target);
                socket.connect(new InetSocketAddress(ip, port), durableExtend.getConnectionTimeoutMs());
                log.info("{} --> connected, ignore clean", target);
            } catch (IOException e) {
                // 连接失败即删除节点
                log.info("{} --> connect failure, execute delete", target);
                registryCenter.remove(path);
            } catch (Exception e) {
                log.error(String.format("%s --> exception", target), e);
            }
        } else {
            log.info("{} --> node can not connect, execute delete", target);
            registryCenter.remove(path);
        }
    }

}
