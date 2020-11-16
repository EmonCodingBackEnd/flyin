package com.coding.flyin.cmp.auth.jwt;

import com.coding.flyin.cmp.auth.metadata.AppSession;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class JwtTokenContext implements Serializable {

    private static final long serialVersionUID = -6246048723520826355L;

    private AppSession jwtSession;

    /** 接口调用令牌. */
    private String currentAuthToken;
}
