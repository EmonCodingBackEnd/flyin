package com.coding.flyin.cmp.auth.jwt;

import java.io.Serializable;

public abstract class JwtToken implements Serializable {

    private static final long serialVersionUID = -8639674655283836834L;

    protected static final String CLAIM_KEY_IDENTITY = "sub";
    protected static final String CLAIM_KEY_METADATA = "metadata";
    //    protected static final String CLAIM_KEY_EXTRADATA = "extraData";
    protected static final String CLAIM_KEY_CREATED = "created";

    /** 默认一周过期. */
    public static long expiration = 604800L;

    /** Token在HTTP请求头中存在的具体属性. */
    public static final String TOKEN_HEADER = "Authorization";

    /** Token值前缀. */
    public static final String TOKEN_PREFIX = "Bearer ";

    public static String fetchToken(String authHeader) {
        String authToken = null;
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            authToken = authHeader.substring(TOKEN_PREFIX.length()); // The part after "Bearer "
        }
        return authToken;
    }
}

/*
authentication
英 [ɔːˌθentɪˈkeɪʃn]   美 [ɔːˌθentɪˈkeɪʃn]
n.
身份验证;认证；鉴定

AppSessionFilter
AppSessionMetadata
	sessionKey
	sessionType


// 根据metadata获取实际session信息
Class<T> clazz fetchSession(AppSessionMetadata  metadata) {
	(clazz)redisClient.get(metadata.getSessionKey())
}
AppSessionKey
AppSession

authorization
英 [ˌɔːθəraɪˈzeɪʃn]
授权

authority
英 [ɔːˈθɒrəti]   美 [əˈθɔːrəti]
n.
权力;威权;当权(地位);权;职权;批准;授权
 */
