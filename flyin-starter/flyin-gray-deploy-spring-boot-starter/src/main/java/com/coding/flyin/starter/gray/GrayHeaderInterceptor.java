package com.coding.flyin.starter.gray;

import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.coding.flyin.starter.gray.request.rule.FilterRequestRule;
import com.coding.flyin.starter.gray.request.rule.RequestRuleFactory;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Setter
@Slf4j
public class GrayHeaderInterceptor extends HandlerInterceptorAdapter {

    private RequestRuleProperties ruleProperties;

    public static final HystrixRequestVariableDefault<FilterRequestRule> rule =
            new HystrixRequestVariableDefault<>();

    public static void initHystrixRequestContext(String labels) {
        log.info("rule: {}", labels);
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }

        FilterRequestRule rule = RequestRuleFactory.create(labels);

        if (rule != null) {
            GrayHeaderInterceptor.rule.set(rule);
        } else {
            GrayHeaderInterceptor.rule.remove();
        }
    }

    public static void shutdownHystrixRequestContext() {
        if (HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.getContextForCurrentThread().shutdown();
        }
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 用当前应用的配置更新传递规则
        String labels = request.getHeader(GrayConstants.RULE_HEADER);
        FilterRequestRule rule = RequestRuleFactory.create(labels);
        labels = ruleProperties.updateRule(rule);
        GrayHeaderInterceptor.initHystrixRequestContext(labels);
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable ModelAndView modelAndView)
            throws Exception {
        GrayHeaderInterceptor.shutdownHystrixRequestContext();
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable Exception ex)
            throws Exception {
        GrayHeaderInterceptor.shutdownHystrixRequestContext();
    }
}
