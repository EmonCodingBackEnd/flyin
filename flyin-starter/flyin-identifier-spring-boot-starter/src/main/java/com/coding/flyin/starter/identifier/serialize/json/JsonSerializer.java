package com.coding.flyin.starter.identifier.serialize.json;

import com.coding.flyin.starter.identifier.serialize.Serializer;

/**
 * json serializer
 */
public abstract class JsonSerializer<T> implements Serializer {

    /**
     * to json string
     * 
     * @param object 待转换对象
     * @return JSON 字符串
     * @throws Exception 系统异常
     */
    public abstract String toJsonString(Object object) throws Exception;

    /**
     * to java object
     * 
     * @param json JSON 字符串
     * @param clazz 对象类型
     * @return java对象
     * @throws Exception 系统异常
     */
    public abstract T parseObject(String json, Class<T> clazz) throws Exception;

}
