package com.coding.flyin.starter.identifier.register;

import java.io.Closeable;

/**
 * ID生成器依赖的连接器
 */
public interface GeneratorConnector extends Closeable {

    /**
     * 初始化数据
     */
    void init();

    /**
     * 连接
     */
    void connect();

    /**
     * 当前是否正常运行
     */
    boolean isWorking();

    /**
     * 是否正在连接
     */
    boolean isConnecting();
}
