package com.coding.flyin.cmp.common.mq;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface AppBaseMqName {

    /**
     * 获取key，不包含变量部分，适用于声明使用，比如 @Cacheable.
     *
     * <p>创建时间: <font style="color:#00FFFF">20190603 10:51</font><br>
     * 比如： prefix:keyPart1:keyPart2......:
     *
     * @return java.lang.String
     * @author Rushing0711
     * @since 1.0.0
     */
    String getName();

    /** Redis的Key前缀. */
    String getPrefix();

    default String getDelimiter() {
        return "_";
    }

    /**
     * 实现该接口的枚举可以在调用该方法.
     *
     * <p>创建时间: <font style="color:#00FFFF">20190327 19:10</font><br>
     * 比如： endWithColon = true, prefix:keyPart1:keyPart2......:<br>
     * 比如： endWithColon = false, prefix:keyPart1:keyPart2......:keyVar
     *
     * @param nameParts - 组成RedisKey的元素集合，不包含前缀，前缀是自动添加的
     * @return java.lang.String
     * @author Rushing0711
     * @since 1.0.0
     */
    default String combineNameParts(String... nameParts) {
        List<Object> keyList = new ArrayList<>();
        keyList.add(getPrefix());
        keyList.addAll(Arrays.asList(nameParts));
        return StringUtils.collectionToDelimitedString(keyList, getDelimiter());
    }
}
