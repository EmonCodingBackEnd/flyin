package com.coding.flyin.cmp.auth.metadata.enums;

import com.coding.flyin.cmp.common.cache.AppBaseRedisKey;
import lombok.Getter;
import lombok.NonNull;

@Getter
public enum AppUrlEnum {
    VERSION("version", "url版本"),
    WHITELIST("whitelist", "url白名单"),
    AUTHORITY("authority", "url授权条件"),
    ;
    private final String key;
    private final String desc;

    AppUrlEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    /**
     * 返回不带有scope的redisKey.<br>
     * instance = TOKEN<br>
     * prefixKey = "prefixKey:10000"<br>
     * fullKey(prefixKey) -> "prefixKey:10000:version"<br>
     */
    public String fullKey(@NonNull String prefixKey) {
        return prefixKey.concat(AppBaseRedisKey.getDelimiter()).concat(this.getKey());
    }
}
