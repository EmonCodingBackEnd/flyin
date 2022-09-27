package com.coding.flyin.starter.identifier.register;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.coding.flyin.starter.identifier.properties.ApplicationProperties;
import com.coding.flyin.starter.identifier.register.zookeeper.NodeInfo;
import com.coding.flyin.starter.identifier.register.zookeeper.NodePath;
import com.coding.flyin.starter.identifier.registry.RegistryCenter;
import com.coding.flyin.starter.identifier.serialize.json.JsonSerializer;
import com.coding.flyin.starter.identifier.util.HostUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Worker注册
 */
// @Data
@Slf4j
public abstract class AbstractMachineRegister implements MachineRegister {

    /**
     * 注册中心工具
     */
    protected RegistryCenter registryCenter;

    /**
     * 应用属性
     */
    protected ApplicationProperties applicationProperties;

    /**
     * zk节点信息
     */
    protected NodePath nodePath;

    /**
     * Json序列化
     */
    protected JsonSerializer<NodeInfo> jsonSerializer;

    /**
     * 检查节点信息
     * 
     * @param localNodeInfo 本地缓存节点信息
     * @param zkNodeInfo zookeeper节点信息
     * @return 节点信息相同返回true，否则返回false
     */
    protected boolean checkNodeInfo(NodeInfo localNodeInfo, NodeInfo zkNodeInfo) {
        try {
            // NodeId、IP、HostName、GroupName 相等（本地缓存==ZK数据）
            if (!zkNodeInfo.getNodeId().equals(localNodeInfo.getNodeId())) {
                return false;
            }
            if (!zkNodeInfo.getIp().equals(localNodeInfo.getIp())) {
                return false;
            }
            if (!zkNodeInfo.getHostName().equals(localNodeInfo.getHostName())) {
                return false;
            }
            if (!zkNodeInfo.getGroupName().equals(localNodeInfo.getGroupName())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("check node info error", e);
            return false;
        }
    }

    /**
     * 缓存机器节点信息至本地
     * 
     * @param nodeInfo 机器节点信息
     * @throws Exception 系统异常
     */
    protected void saveLocalNodeInfo(NodeInfo nodeInfo) throws Exception {
        try {
            File nodeInfoFile = new File(applicationProperties.getRegistryFile());
            String nodeInfoJson = jsonizeNodeInfo(nodeInfo);
            FileUtils.writeStringToFile(nodeInfoFile, nodeInfoJson, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("save node info cache error", e);
        }
    }

    /**
     * 读取本地缓存机器节点
     * 
     * @return 机器节点信息
     */
    protected NodeInfo getLocalNodeInfo() {
        try {
            File nodeInfoFile = new File(applicationProperties.getRegistryFile());
            if (nodeInfoFile.exists()) {
                String nodeInfoJson = FileUtils.readFileToString(nodeInfoFile, StandardCharsets.UTF_8);
                return createNodeInfoFromJsonStr(nodeInfoJson);
            }
        } catch (Exception e) {
            log.error("read node info cache error", e);
        }
        return null;
    }

    /**
     * 初始化节点信息
     * 
     * @param groupName 分组名
     * @param machineId 机器号
     * @return 节点信息
     * @throws UnknownHostException 未知HOST异常
     */
    protected NodeInfo createNodeInfo(String groupName, Integer machineId) throws UnknownHostException {
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setNodeId(genNodeId());
        nodeInfo.setGroupName(groupName);
        nodeInfo.setMachineId(machineId);
        nodeInfo.setIp(HostUtils.getLocalIP());
        nodeInfo.setPort(applicationProperties.getPort());
        nodeInfo.setHostName(HostUtils.getLocalHostName());
        nodeInfo.setCreateTime(new Date());
        nodeInfo.setUpdateTime(new Date());
        return nodeInfo;
    }

    /**
     * 通过节点信息JSON字符串反序列化节点信息
     * 
     * @param jsonStr 节点信息JSON字符串
     * @return 节点信息
     * @throws Exception 系统异常
     */
    protected NodeInfo createNodeInfoFromJsonStr(String jsonStr) throws Exception {
        return jsonSerializer.parseObject(jsonStr, NodeInfo.class);
    }

    /**
     * 节点信息转json字符串
     * 
     * @param nodeInfo 节点信息
     * @return json字符串
     * @throws Exception 系统异常
     */
    protected String jsonizeNodeInfo(NodeInfo nodeInfo) throws Exception {
        return jsonSerializer.toJsonString(nodeInfo);
    }

    /**
     * 获取本地节点缓存文件路径
     * 
     * @param groupName 分组名
     * @return 文件路径
     */
    protected String getDefaultFilePath(String groupName) {
        StringBuilder builder = new StringBuilder();
        builder.append(".").append(File.separator).append("tmp");
        builder.append(File.separator).append("flyin").append(File.separator).append("identifier");
        builder.append(File.separator).append(groupName).append(".cache");
        return builder.toString();
    }

    /**
     * 获取节点唯一ID （基于UUID）
     * 
     * @return 节点唯一ID
     */
    protected String genNodeId() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 获取节点ZK Path Key
     * 
     * @param nodePath 节点路径信息
     * @param machineId 节点机器ID
     * @return 节点PATH的KEY
     */
    protected String getNodePathKey(NodePath nodePath, Integer machineId) {
        StringBuilder builder = new StringBuilder();
        builder.append(nodePath.getMachinePath()).append("/");
        builder.append(machineId);
        return builder.toString();
    }

}
