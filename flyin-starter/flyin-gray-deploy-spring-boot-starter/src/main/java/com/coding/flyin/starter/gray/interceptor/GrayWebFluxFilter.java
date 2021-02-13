package com.coding.flyin.starter.gray.interceptor;

import com.coding.flyin.starter.gray.constant.GrayConstants;
import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import com.coding.flyin.starter.gray.rule.filter.RuleFilter;
import com.coding.flyin.starter.gray.rule.filter.RuleFilterFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Setter
@Slf4j
public class GrayWebFluxFilter implements GlobalFilter, Ordered {

    private RequestRuleProperties ruleProperties;

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 10;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 用当前应用的配置更新传递规则
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        String reqLabels = httpHeaders.getFirst(GrayConstants.RULE_HEADER);
        RuleFilter rule = RuleFilterFactory.create(reqLabels);
        String effectiveLabels = ruleProperties.updateRule(rule);
        log.info("req rule: {} and effective rule: {}", reqLabels, effectiveLabels);
        GrayInterceptorHelper.initHystrixRequestContext(effectiveLabels);
        return chain.filter(exchange)
                .then(Mono.just(exchange))
                .map(
                        serverWebExchange -> {
                            GrayInterceptorHelper.shutdownHystrixRequestContext();
                            return serverWebExchange;
                        })
                .then();
    }
}
