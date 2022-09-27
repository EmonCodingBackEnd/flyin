package com.coding.flyin.starter.identifier.register;

import com.coding.flyin.starter.identifier.register.zookeeper.NodeInfo;

/**
 * Machine注册
 */
public interface MachineRegister {

    /**
     * 最大机器数
     */
    long MAX_MACHINE_NUM = 1024;

    /**
     * 加锁最大等待时间，30s
     */
    int MAX_LOCK_WAIT_TIME_MS = 30 * 1000;

    /**
     * 注册成功的节点信息
     * 
     * @return 注册成功的节点信息
     */
    NodeInfo register();

    /**
     * 退出注册
     */
    void logout();
}
