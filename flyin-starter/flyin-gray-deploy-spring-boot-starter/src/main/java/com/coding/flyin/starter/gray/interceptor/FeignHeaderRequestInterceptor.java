package com.coding.flyin.starter.gray.interceptor;

import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.coding.flyin.starter.gray.rule.filter.RuleFilter;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class FeignHeaderRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RuleFilter rule = GrayInterceptorHelper.rule.get();
        if (rule != null) {
            requestTemplate.header(GrayConstants.RULE_HEADER, rule.toRule());
        }
    }
}
