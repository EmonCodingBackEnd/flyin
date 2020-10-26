package com.coding.flyin.starter.gray.filter;

import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.coding.flyin.starter.gray.interceptor.GrayInterceptorHelper;
import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import com.coding.flyin.starter.gray.request.rule.FilterRequestRule;
import com.coding.flyin.starter.gray.request.rule.RequestRuleFactory;
import lombok.Setter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
public class MvcGrayFilter extends OncePerRequestFilter {

    private RequestRuleProperties ruleProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 用当前应用的配置更新传递规则
        String labels = request.getHeader(GrayConstants.RULE_HEADER);
        FilterRequestRule rule = RequestRuleFactory.create(labels);
        labels = ruleProperties.updateRule(rule);
        GrayInterceptorHelper.initHystrixRequestContext(labels);
        try {
            filterChain.doFilter(request, response);
        } finally {
            GrayInterceptorHelper.shutdownHystrixRequestContext();
        }
    }
}
