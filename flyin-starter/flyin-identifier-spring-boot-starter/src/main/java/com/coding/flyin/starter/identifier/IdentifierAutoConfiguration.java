package com.coding.flyin.starter.identifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.coding.flyin.starter.identifier.generator.IdentifierGenerator;
import com.coding.flyin.starter.identifier.generator.SnowflakeGenerator;
import com.coding.flyin.starter.identifier.properties.ApplicationProperties;
import com.coding.flyin.starter.identifier.properties.ZookeeperProperties;
import com.coding.flyin.starter.identifier.register.zookeeper.ZookeeperMachineRegister;
import com.coding.flyin.starter.identifier.registry.zookeeper.ZookeeperRegistryCenter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnProperty(prefix = "flyin.identifier", name = "enabled", havingValue = "true")
@EnableConfigurationProperties({ZookeeperProperties.class, ApplicationProperties.class})
@Slf4j
public class IdentifierAutoConfiguration {

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public SnowflakeGenerator snowflakeGenerator() {
        ZookeeperRegistryCenter registryCenter = new ZookeeperRegistryCenter(zookeeperProperties);
        ZookeeperMachineRegister workerRegister = new ZookeeperMachineRegister(registryCenter, applicationProperties);
        SnowflakeGenerator generator = new SnowflakeGenerator(workerRegister);
        generator.init();
        return generator;
    }

    @Bean
    public IdentifierGenerator<Long> identifierGenerator() {
        return snowflakeGenerator();
    }
}
