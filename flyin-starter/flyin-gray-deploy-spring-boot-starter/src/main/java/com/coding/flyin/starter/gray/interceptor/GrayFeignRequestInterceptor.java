package com.coding.flyin.starter.gray.interceptor;

import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.coding.flyin.starter.gray.rule.filter.RuleFilter;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Setter
@Slf4j
public class GrayFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RuleFilter rule = GrayInterceptorHelper.rule.get();
        if (rule != null) {
            requestTemplate.header(GrayConstants.RULE_HEADER, rule.toRule());
        }

        MultiValueMap<String, String> headers = GrayInterceptorHelper.headers.get();
        if (headers != null) {
            for (Map.Entry<String, List<String>> header : headers.entrySet()) {
                if (!CollectionUtils.isEmpty(header.getValue())) {
                    if (header.getValue().size() > 1) {
                        log.error(
                                "【Feign异常】发现 key={} 的请求头有 {} 个值",
                                header.getKey(),
                                header.getValue().size());
                    }
                }
                requestTemplate.header(header.getKey(), header.getValue());
            }
        }
    }
}
