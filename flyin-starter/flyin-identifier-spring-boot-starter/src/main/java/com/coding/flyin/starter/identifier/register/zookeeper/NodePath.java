package com.coding.flyin.starter.identifier.register.zookeeper;

import lombok.Data;

/**
 * 机器节点Path
 */
@Data
public class NodePath {

    private static final String WORKER_NODE = "machine";

    /**
     * workerId分组名
     */
    private String groupName;

    /**
     * 机器编号
     */
    private long machineId;

    private long sessionId = -1L;

    public NodePath(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupPath() {
        return String.format("/%s", groupName);
    }

    public String getMachinePath() {
        return String.format("/%s/%s", groupName, WORKER_NODE);
    }

    public String getMachineIdPath() {
        return String.format("/%s/%s/%s", groupName, WORKER_NODE, machineId);
    }

    public String getGroupName() {
        return groupName;
    }

}
