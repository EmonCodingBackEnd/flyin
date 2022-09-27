package com.coding.flyin.starter.identifier.register.zookeeper;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class NodeInfo implements Serializable {
    private static final long serialVersionUID = 2955140198241011384L;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 节点归属组名
     */
    private String groupName;

    /**
     * 注册得到的机器ID
     */
    private Integer machineId;

    /**
     * 数据中心ID
     */
    @Setter(AccessLevel.NONE)
    private long dataCenterId;

    /**
     * 工作进程ID
     */
    @Setter(AccessLevel.NONE)
    private long workerId;

    /**
     * 本实例IP地址
     */
    private String ip;

    /**
     * 本实例探测端口
     */
    private Integer port;

    /**
     * 主机名
     */
    private String hostName;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
        // 1 - 41 - 5 - 5 - 12 其中 5 和 5 表示数据中心ID的5位和工作进程ID的5位
        // 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
        String machineBinaryString = StringUtils.leftPad(Long.toBinaryString(this.machineId << 12), 64, '0');
        String dataCenterBinaryString = machineBinaryString.substring(1 + 41, 1 + 41 + 5);
        this.dataCenterId = Long.parseLong(dataCenterBinaryString, 2);
        String workerIdBinaryString = machineBinaryString.substring(1 + 41 + 5, 1 + 41 + 5 + 5);
        this.workerId = Long.parseLong(workerIdBinaryString, 2);
    }

}
