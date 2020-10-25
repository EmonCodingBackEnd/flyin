package com.coding.flyin.starter.gray.request.rule;

import com.netflix.loadbalancer.Server;

import java.util.Collection;

public interface FilterRequestRule {

    Collection<Server> filter(Collection<Server> serverList);

    String toRule();
}
