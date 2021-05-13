package com.coding.flyin.cmp.auth.metadata.enums;

import com.coding.flyin.cmp.auth.metadata.AppMetadata;
import com.coding.flyin.cmp.common.cache.AppBaseRedisKey;
import lombok.Getter;
import lombok.NonNull;

@Getter
public enum AppAuthEnum {
    TOKEN("token", "authToken"),
    SESSION("session", "用户缓存信息"),
    PERMISSON("permisson", "用户权限数据"),
    ;
    private final String key;
    private final String desc;

    AppAuthEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    /**
     * 返回不带有scope的redisKey.<br>
     * 场景举例：AppAuthEnum的instance = TOKEN<br>
     * prefixKey = "prefixKey:10000"<br>
     * fullKey(prefixKey) -> "prefixKey:10000:token"<br>
     */
    public String fullKey(@NonNull String prefixKey) {
        return prefixKey.concat(AppBaseRedisKey.getDelimiter()).concat(this.getKey());
    }

    /**
     * 返回带有scope的redisKey.<br>
     * 场景举例：AppAuthEnum的instance  = SESSION<br>
     * metadata.scope = "default"<br>
     * fullKey(metadata) -> "session:default"<br>
     */
    public String scopeKey(@NonNull AppMetadata metadata) {
        return this.getKey().concat(AppBaseRedisKey.getDelimiter()).concat(metadata.getScope());
    }

    /**
     * 返回带有scope的full redisKey.<br>
     * 场景举例：AppAuthEnum的instance = PERMISSON<br>
     * metadata.prefixKey = "prefixKey:10000"<br>
     * metadata.scope = "default"<br>
     * fullKey(metadata) -> "prefixKey:10000:permisson:default"<br>
     */
    private String scopeFullKey(@NonNull AppMetadata metadata) {
        return fullKey(metadata.getPrefixKey())
                .concat(AppBaseRedisKey.getDelimiter())
                .concat(metadata.getScope());
    }
}
