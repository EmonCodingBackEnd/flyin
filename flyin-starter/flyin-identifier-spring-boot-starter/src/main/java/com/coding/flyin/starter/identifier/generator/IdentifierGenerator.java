package com.coding.flyin.starter.identifier.generator;

/**
 * ID生成器接口
 */
public interface IdentifierGenerator<T> {

    /**
     * 生成下一个ID
     * 
     * @return - id
     */
    T nextId();

    /**
     * 批量生成ID.
     * <p>
     * 最大批量数：10个序列的掩码时间，(sequenceMask(4095) + 1) * 10 = 40960个
     * </p>
     * 
     * @param size - 批量数
     * @return - id
     */
    T[] nextId(int size);

}
