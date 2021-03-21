package com.coding.flyin.starter.gray.interceptor;

import com.coding.flyin.starter.gray.rule.filter.RuleFilter;
import com.coding.flyin.starter.gray.rule.filter.RuleFilterFactory;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public abstract class GrayInterceptorHelper {

    public static final HystrixRequestVariableDefault<RuleFilter> rule =
            new HystrixRequestVariableDefault<>();

    public static final HystrixRequestVariableDefault<MultiValueMap<String, String>> headers =
            new HystrixRequestVariableDefault<>();

    public static void initHystrixRequestContext(String labels) {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }

        RuleFilter rule = RuleFilterFactory.create(labels);

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

    public static List<String> getHeader(String key) {
        Assert.notNull(key, "Only non-null key instances are permitted");
        if (GrayInterceptorHelper.headers.get() == null) {
            GrayInterceptorHelper.headers.set(new LinkedMultiValueMap<>());
        }
        return GrayInterceptorHelper.headers.get().getOrDefault(key, new LinkedList<>());
    }

    public static String getFirstHeader(String key) {
        Assert.notNull(key, "Only non-null key instances are permitted");
        if (GrayInterceptorHelper.headers.get() == null) {
            GrayInterceptorHelper.headers.set(new LinkedMultiValueMap<>());
        }
        return GrayInterceptorHelper.headers.get().getFirst(key);
    }

    public static void addHeader(String key, String value) {
        Assert.notNull(key, "Only non-null key instances are permitted");
        Assert.notNull(value, "Only non-null value instances are permitted");
        if (GrayInterceptorHelper.headers.get() == null) {
            GrayInterceptorHelper.headers.set(new LinkedMultiValueMap<>());
        }
        GrayInterceptorHelper.headers.get().add(key, value);
    }

    public static List<String> removeHeader(String key) {
        Assert.notNull(key, "Only non-null key instances are permitted");
        if (GrayInterceptorHelper.headers.get() == null) {
            GrayInterceptorHelper.headers.set(new LinkedMultiValueMap<>());
        }
        return GrayInterceptorHelper.headers.get().remove(key);
    }

    public static List<String> replaceHeader(String key, String value) {
        Assert.notNull(key, "Only non-null key instances are permitted");
        Assert.notNull(value, "Only non-null value instances are permitted");
        if (GrayInterceptorHelper.headers.get() == null) {
            GrayInterceptorHelper.headers.set(new LinkedMultiValueMap<>());
        }
        List<String> valueList = new LinkedList<>();
        valueList.add(value);
        return GrayInterceptorHelper.headers.get().replace(key, valueList);
    }

    public static List<String> replaceHeader(String key, LinkedList<String> valueList) {
        Assert.notNull(key, "Only non-null key instances are permitted");
        Assert.notNull(valueList, "Only non-null value instances are permitted");
        if (GrayInterceptorHelper.headers.get() == null) {
            GrayInterceptorHelper.headers.set(new LinkedMultiValueMap<>());
        }
        return GrayInterceptorHelper.headers.get().replace(key, valueList);
    }
}
