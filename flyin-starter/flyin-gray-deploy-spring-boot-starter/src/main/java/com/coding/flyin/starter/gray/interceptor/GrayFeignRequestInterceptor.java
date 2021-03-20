package com.coding.flyin.starter.gray.interceptor;

import com.coding.flyin.core.GlobalConstants;
import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.coding.flyin.starter.gray.rule.filter.RuleFilter;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class GrayFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RuleFilter rule = GrayInterceptorHelper.rule.get();
        String traceId = GrayInterceptorHelper.trace.get();
        if (rule != null) {
            requestTemplate.header(GrayConstants.RULE_HEADER, rule.toRule());
            requestTemplate.header(GlobalConstants.TRACE_ID, traceId);
        }
    }
}
