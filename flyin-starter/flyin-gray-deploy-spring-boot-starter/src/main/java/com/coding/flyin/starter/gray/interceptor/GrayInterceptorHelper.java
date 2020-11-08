package com.coding.flyin.starter.gray.interceptor;

import com.coding.flyin.starter.gray.request.rule.FilterRequestRule;
import com.coding.flyin.starter.gray.request.rule.RequestRuleFactory;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GrayInterceptorHelper {

    public static final HystrixRequestVariableDefault<FilterRequestRule> rule =
            new HystrixRequestVariableDefault<>();

    public static void initHystrixRequestContext(String labels) {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }

        FilterRequestRule rule = RequestRuleFactory.create(labels);

        if (rule != null) {
            GrayInterceptorHelper.rule.set(rule);
        } else {
            GrayInterceptorHelper.rule.remove();
        }
    }

    public static void shutdownHystrixRequestContext() {
        if (HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.getContextForCurrentThread().shutdown();
        }
    }
}
