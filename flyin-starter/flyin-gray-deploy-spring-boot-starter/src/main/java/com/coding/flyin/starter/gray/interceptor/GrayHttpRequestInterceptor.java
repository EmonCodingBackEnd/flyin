package com.coding.flyin.starter.gray.interceptor;

import com.coding.flyin.core.GlobalConstants;
import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.coding.flyin.starter.gray.rule.filter.RuleFilter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

@Setter
@Slf4j
public class GrayHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
        RuleFilter rule = GrayInterceptorHelper.rule.get();
        String traceId = GrayInterceptorHelper.trace.get();
        if (rule != null) {
            requestWrapper.getHeaders().add(GrayConstants.RULE_HEADER, rule.toRule());
            requestWrapper.getHeaders().add(GlobalConstants.TRACE_ID, traceId);
        }
        return execution.execute(request, body);
    }
}
