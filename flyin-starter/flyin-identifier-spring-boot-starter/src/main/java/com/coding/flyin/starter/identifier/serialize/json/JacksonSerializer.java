package com.coding.flyin.starter.identifier.serialize.json;

import java.text.SimpleDateFormat;

import com.coding.flyin.starter.identifier.serialize.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * jackson serializer
 */
public class JacksonSerializer<T> extends JsonSerializer<T> {

    private final ObjectMapper objectMapper;

    public JacksonSerializer() {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Serializer.DEFAULT_DATE_FORMAT));
    }

    @Override
    public String toJsonString(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Override
    public T parseObject(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, clazz);
    }
}
