package com.coding.flyin.cmp.auth.metadata;

import com.coding.flyin.cmp.auth.metadata.enums.AppAuthEnum;
import com.coding.flyin.cmp.exception.AppException;
import com.coding.flyin.cmp.exception.AppStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class AppMetadata<T extends AppSession> implements Serializable {

    private static final long serialVersionUID = -5111511704264251017L;

    /** 用户登录时的认证类型 */
    private String authType;

    /**
     * 设计如下：<br>
     * 对每一个登录的用户，一般来讲会保存如下几部分信息：<br>
     *
     * <ol>
     *   <li>jwtToken
     *   <li>用户的认证类型（比如：用户名密码登录、微信扫码登录、第三方授权登录等等）
     *   <li>用户主要信息的 session
     *   <li>用户的权限信息：用于授权
     * </ol>
     *
     * 针对以上各种情况，一个用户可以保存多个缓存信息；这些信息有通用的 prefixKey 和 专有标志信息拼接成最终的 redisKey。<br>
     * 专有标志信息参考 {@linkplain AppAuthEnum 用户缓存类型定义枚举 }
     */
    private String prefixKey;

    /** 对于一个用户来讲，可能在不同的 租户 下有不同的用户缓存信息， scope 表示当前在某个租户下。 */
    private String scope = "default";

    private Class<T> clazz;

    /** 为一个token维护一份轻量级数据，控制为50个字符以内. */
    private String lightData;

    public void setLightData(String lightData) {
        if (lightData != null && lightData.length() > 50) {
            throw new AppException(AppStatus.S0001, "Exceeding the maximum length of 50");
        }
        this.lightData = lightData;
    }

    public static AppMetadata getInstance() {
        return new AppMetadata();
    }
}
