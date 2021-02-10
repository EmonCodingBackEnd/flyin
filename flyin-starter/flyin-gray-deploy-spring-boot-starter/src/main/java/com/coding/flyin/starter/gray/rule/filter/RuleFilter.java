package com.coding.flyin.starter.gray.rule.filter;

import com.netflix.loadbalancer.Server;

import java.util.Collection;

public interface RuleFilter {

    Collection<Server> filter(Collection<Server> serverList);

    String toRule();
}
