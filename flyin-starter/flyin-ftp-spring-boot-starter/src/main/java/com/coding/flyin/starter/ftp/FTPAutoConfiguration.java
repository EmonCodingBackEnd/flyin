package com.coding.flyin.starter.ftp;

import com.coding.flyin.starter.ftp.pool.GenericFTPClientPool;
import com.coding.flyin.starter.ftp.pool.PooledFTPClientFactory;
import com.coding.flyin.starter.ftp.properties.PooledFTPProperties;
import com.coding.flyin.starter.ftp.template.FTPTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @EnableConfigurationProperties(PooledFTPProperties.class) 方式一：第二步
@ConditionalOnClass({FTPClient.class, FTPTemplate.class})
@ConditionalOnProperty(prefix = "flyin.ftp", name = "enabled", havingValue = "true")
@Slf4j
public class FTPAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "flyin.ftp") // 方式二：第一步
    public PooledFTPProperties ftpProperties() {
        return new PooledFTPProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public FTPTemplate ftpTemplate() {
        PooledFTPProperties ftpProperties = ftpProperties();
        log.info("【FTP】连接配置 ftpProperties={}", ftpProperties);

        log.info("【FTP】Init FTPTemplate");
        // ftpClient工厂配置
        PooledFTPClientFactory factory = new PooledFTPClientFactory(ftpProperties.getServer());
        // 组合工厂和连接池
        GenericFTPClientPool pool = new GenericFTPClientPool(factory, ftpProperties.getPool());
        return new FTPTemplate(pool);
    }

    @Bean
    @ConditionalOnMissingBean
    public FTPTools ftpTools() {
        FTPTemplate ftpTemplate = ftpTemplate();
        log.info("【FTP】Init FTPTools");
        return new FTPTools(ftpTemplate);
    }
}
