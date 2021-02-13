package com.coding.flyin.starter.gray.configuration;

import com.coding.flyin.starter.gray.interceptor.GrayFeignRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

/** 微服务通过 feign.RequestInterceptor 传递参数 */
@ConditionalOnClass(name = {"feign.RequestInterceptor"})
@RefreshScope
public class FeignConfiguration {

    @Bean
    public GrayFeignRequestInterceptor grayFeignRequestInterceptor() {
        return new GrayFeignRequestInterceptor();
    }
}
