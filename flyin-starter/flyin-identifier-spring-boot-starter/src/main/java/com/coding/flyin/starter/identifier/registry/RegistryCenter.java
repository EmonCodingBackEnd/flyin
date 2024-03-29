package com.coding.flyin.starter.identifier.registry;

import java.util.List;

/**
 * 注册中心
 */
public interface RegistryCenter {

    /**
     * 初始化注册中心.
     */
    void init();

    /**
     * 关闭注册中心.
     */
    void close();

    /**
     * 优先从本地缓存获取注册数据，如果不存在，再从注册中心直接获取.【含缓存】
     * 
     * @param key 键
     * @return 值
     */
    String get(String key);

    /**
     * 直接从注册中心而非本地缓存获取数据.
     *
     * @param key 键
     * @return 值
     */
    String getDirectly(String key);

    /**
     * 获取子节点名称集合.
     *
     * @param key 键
     * @return 子节点名称集合
     */
    List<String> getChildrenKeys(String key);

    /**
     * 获取子节点数量.
     *
     * @param key 键
     * @return 子节点数量
     */
    int getNumChildren(String key);

    /**
     * 获取数据是否存在.
     * 
     * @param key 键
     * @return 数据是否存在
     */
    boolean isExisted(String key);

    /**
     * 持久化注册数据.
     * 
     * @param key 键
     * @param value 值
     */
    void persist(String key, String value);

    /**
     * 更新注册数据.
     * 
     * @param key 键
     * @param value 值
     */
    void update(String key, String value);

    /**
     * 持久化临时注册数据.
     *
     * @param key 键
     * @param value 值
     */
    void persistEphemeral(String key, String value);

    /**
     * 持久化顺序注册数据.
     *
     * @param key 键
     * @param value 值
     * @return 包含10位顺序数字的znode名称
     */
    String persistSequential(String key, String value);

    /**
     * 持久化临时顺序注册数据.
     *
     * @param key 键
     */
    void persistEphemeralSequential(String key);

    /**
     * 删除注册数据.
     * 
     * @param key 键
     */
    void remove(String key);

    /**
     * 获取注册中心当前时间.
     * 
     * @param key 用于获取时间的键
     * @return 注册中心当前时间
     */
    long getRegistryCenterTime(String key);

    /**
     * 直接获取操作注册中心的原生客户端. 如：Zookeeper或Redis等原生客户端.
     * 
     * @return 注册中心的原生客户端
     */
    Object getRawClient();

    /**
     * 添加本地缓存.【仅缓存】
     *
     * @param cachePath 需加入缓存的路径
     */
    void addCacheData(String cachePath);

    /**
     * 释放本地缓存.【仅缓存】
     *
     * @param cachePath 需释放缓存的路径
     */
    void evictCacheData(String cachePath);

    /**
     * 获取注册中心数据缓存对象.【仅缓存】
     *
     * @param cachePath 缓存的节点路径
     * @return 注册中心数据缓存对象
     */
    Object getRawCache(String cachePath);
}
