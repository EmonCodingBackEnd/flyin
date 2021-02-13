package com.coding.flyin.starter.gray.interceptor;

import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import com.coding.flyin.starter.gray.rule.filter.RuleFilter;
import com.coding.flyin.starter.gray.rule.filter.RuleFilterFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
@Slf4j
public class GrayWebMvcFilter extends OncePerRequestFilter {

    private RequestRuleProperties ruleProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 用当前应用的配置更新传递规则
        String reqLabels = request.getHeader(GrayConstants.RULE_HEADER);
        RuleFilter rule = RuleFilterFactory.create(reqLabels);
        String effectiveLabels = ruleProperties.updateRule(rule);
        log.info("req rule: {} and effective rule: {}", reqLabels, effectiveLabels);
        GrayInterceptorHelper.initHystrixRequestContext(effectiveLabels);
        try {
            filterChain.doFilter(request, response);
        } finally {
            GrayInterceptorHelper.shutdownHystrixRequestContext();
        }
    }
}