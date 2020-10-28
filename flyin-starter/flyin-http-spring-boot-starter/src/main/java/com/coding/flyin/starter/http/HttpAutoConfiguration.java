package com.coding.flyin.starter.http;

import com.coding.flyin.starter.http.generator.HttpSyncClientGenerator;
import com.coding.flyin.starter.http.properties.HttpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HttpProperties.class)
@ConditionalOnClass({HttpClient.class})
@Slf4j
public class HttpAutoConfiguration {

    @Autowired private HttpProperties httpProperties;

    @Bean
    @ConditionalOnMissingBean
    public HttpSyncTools httpSyncTools() {
        log.info("【Http】Init httpSyncTools, 连接配置 httpProperties={}", httpProperties);

        HttpSyncClientGenerator generator = new HttpSyncClientGenerator(httpProperties);
        CloseableHttpClient httpSyncClient = generator.pool().timeout().build();
        return new HttpSyncTools(httpSyncClient);
    }
}
