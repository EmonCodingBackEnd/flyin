package com.coding.flyin.starter.gray;

import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.coding.flyin.starter.gray.request.rule.FilterRequestRule;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class FeignHeaderRequestInterceptor implements RequestInterceptor {

    private RequestRuleProperties ruleProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        FilterRequestRule rule = GrayHeaderInterceptor.rule.get();
        if (rule != null) {
            requestTemplate.header(GrayConstants.RULE_HEADER, rule.toRule());
        }
    }
}
