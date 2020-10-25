package com.coding.flyin.cmp.idempotent.annotation.resolver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "flyin.nonidempotent")
public class NonIdempotentConfig {

    /** 幂等性校验所需的redis缓存key，默认：<code>flyin:nonidempotent</code>. */
    private String redisKey = "flyin:nonidempotent";
}
