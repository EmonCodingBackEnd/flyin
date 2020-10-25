package com.coding.flyin.cmp.api.annotation;

import lombok.Getter;

@Getter
public enum AppChannel {
    GATEWAY("gateway", "网关"),
    MICRO_CLIENT("microClient", "微服务客户端"),
    DIRECT("direct", "直接访问"),
    ;

    private String channel;
    private String name;

    AppChannel(String channel, String name) {
        this.channel = channel;
        this.name = name;
    }
}
