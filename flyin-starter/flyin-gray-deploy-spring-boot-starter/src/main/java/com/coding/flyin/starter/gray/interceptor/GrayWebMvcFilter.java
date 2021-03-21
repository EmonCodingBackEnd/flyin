package com.coding.flyin.starter.gray.interceptor;

import com.coding.flyin.cmp.auth.jwt.JwtToken;
import com.coding.flyin.core.GlobalConstants;
import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import com.coding.flyin.starter.gray.rule.filter.RuleFilter;
import com.coding.flyin.starter.gray.rule.filter.RuleFilterFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
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

        String traceId = request.getHeader(GlobalConstants.TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            traceId = "TID:N/A";
        }
        String authHeader = request.getHeader(JwtToken.TOKEN_HEADER);
        MDC.put(GlobalConstants.TRACE_ID, traceId);

        RuleFilter rule = RuleFilterFactory.create(reqLabels);
        String effectiveLabels = ruleProperties.updateRule(rule);
        log.info("req rule: {} and effective rule: {}", reqLabels, effectiveLabels);
        GrayInterceptorHelper.initHystrixRequestContext(effectiveLabels);

        GrayInterceptorHelper.addHeader(GlobalConstants.TRACE_ID, traceId);
        GrayInterceptorHelper.addHeader(JwtToken.TOKEN_HEADER, authHeader);
        try {
            filterChain.doFilter(request, response);
        } finally {
            GrayInterceptorHelper.shutdownHystrixRequestContext();
        }
    }
}
