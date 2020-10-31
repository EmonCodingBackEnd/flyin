package com.coding.flyin.starter.ftp.properties;

import lombok.Data;

@Data
// @ConfigurationProperties(prefix = "flyin.ftp") 方式一：第一步
public class PooledFTPProperties {

    private boolean enabled = false;

    /** ftp连接池配置：默认 存在默认配置. */
    private PoolConfig pool = new PoolConfig();

    /** ftp服务器配置. */
    private ServerConfig server;

    @Override
    public String toString() {
        return "PooledFTPProperties{" + "pool=" + pool + ", server=" + server + '}';
    }
}
