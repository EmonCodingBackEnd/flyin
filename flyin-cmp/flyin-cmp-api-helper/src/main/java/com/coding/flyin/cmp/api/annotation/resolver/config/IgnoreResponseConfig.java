package com.coding.flyin.cmp.api.annotation.resolver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "flyin.response-scan")
public class IgnoreResponseConfig {

    /** 统一应答需要管控的包. */
    private Set<String> basePackages;

    public boolean containsPackage(String packageName) {
        if (basePackages == null || basePackages.size() == 0) {
            return true;
        }
        if (packageName == null) {
            return false;
        }
        return basePackages.stream().anyMatch(packageName::startsWith);
    }
}
