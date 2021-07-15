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
     * 场景举例：AppAuthEnum的instance = SESSION<br>
     * metadata.scope = "default"<br>
     * fullKey(metadata) -> "session:default"<br>
     */
    public String scopeKey(@NonNull AppMetadata metadata) {
        return scopeKey(metadata.getScope());
    }

    /**
     * 返回带有scope的redisKey.<br>
     *
     * <p>创建时间: <font style="color:#00FFFF">20210630 09:48</font><br>
     * 场景举例：AppAuthEnum的instance = SESSION<br>
     * scope = "default"<br>
     * fullKey(metadata) -> "session:default"<br>
     *
     * @param scope - 不可为空的值，表示某一个业务范围，比如：某个机构，某个门店等
     * @return java.lang.String
     * @author emon
     * @since 0.1.30
     */
    public String scopeKey(@NonNull String scope) {
        return this.getKey().concat(AppBaseRedisKey.getDelimiter()).concat(scope);
    }

    /**
     * 返回带有scope的full redisKey.<br>
     * 场景举例：AppAuthEnum的instance = PERMISSON<br>
     * metadata.prefixKey = "prefixKey:10000"<br>
     * metadata.scope = "default"<br>
     * fullKey(metadata) -> "prefixKey:10000:permisson:default"<br>
     */
    public String scopeFullKey(@NonNull AppMetadata metadata) {
        return scopeFullKey(metadata.getPrefixKey(), metadata.getScope());
    }

    /**
     * 返回带有scope的full redisKey.<br>
     *
     * <p>创建时间: <font style="color:#00FFFF">20210630 09:51</font><br>
     * 场景举例：AppAuthEnum的instance = PERMISSON<br>
     * prefixKey = "prefixKey:10000"<br>
     * scope = "default"<br>
     * fullKey(metadata) -> "prefixKey:10000:permisson:default"<br>
     *
     * @param prefixKey - redisKey的前缀，例如表示token的 <code>String prefixKey = AppAuthEnum.TOKEN.getKey(userId)</code>
     * @param scope - 不可为空的值，表示某一个业务范围，比如：某个机构，某个门店等
     * @return java.lang.String
     * @author emon
     * @since 0.1.30
     */
    public String scopeFullKey(@NonNull String prefixKey, @NonNull String scope) {
        return fullKey(prefixKey).concat(AppBaseRedisKey.getDelimiter()).concat(scope);
    }
}
