package com.coding.flyin.cmp.generator;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

/**
 * 雪花ID生成器.
 *
 * <p>创建时间: <font style="color:#00FFFF">20201107 13:42</font><br>
 * [请在此输入功能详述]
 *
 * @author emon
 * @version 1.0.0
 * @since 1.0.0
 */
public class SnowFlakeIdMyBatisPlusStrategy implements IdentifierGenerator {

    private final SnowFlakeIdGenerator idGenerator;

    /**
     * 默认构造器.
     *
     * <p>创建时间: <font style="color:#00FFFF">20201107 14:06</font><br>
     * 采用默认数据中心ID(获取mac地址的低16位运算后作为数据中心ID)和默认工作ID初始化
     *
     * @author emon
     * @since 1.0.0
     */
    public SnowFlakeIdMyBatisPlusStrategy() {
        this.idGenerator = new SnowFlakeIdGenerator();
    }

    /**
     * 构造器.
     *
     * <p>创建时间: <font style="color:#00FFFF">20201107 14:06</font><br>
     * 采用指定数据中心ID和工作ID初始化
     *
     * @param datacenterId - 指定的数据中心ID(0-31)
     * @param workerId - 指定的工作ID(0-31)
     */
    public SnowFlakeIdMyBatisPlusStrategy(long datacenterId, long workerId) {
        this.idGenerator = new SnowFlakeIdGenerator(datacenterId, workerId);
    }

    public SnowFlakeIdMyBatisPlusStrategy(SnowFlakeIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public Long nextId(Object entity) {
        return idGenerator.nextId();
    }
}
