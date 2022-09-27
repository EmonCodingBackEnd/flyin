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
     * 批量生成ID
     * 
     * @param size - 批量数
     * @return - id
     */
    T[] nextId(int size);

}
