package com.coding.flyin.starter.identifier.serialize.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * fastjson serializer
 */
public class FastJsonSerializer<T> extends JsonSerializer<T> {

    @Override
    public String toJsonString(Object object) throws Exception {
        return JSON.toJSONStringWithDateFormat(object, DEFAULT_DATE_FORMAT, SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public T parseObject(String json, Class<T> clazz) throws Exception {
        return JSON.parseObject(json, clazz);
    }
}
