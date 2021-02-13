package com.coding.flyin.starter.gray.configuration;

import com.coding.flyin.starter.gray.annotation.DisableGrayWebFluxFilter;
import com.coding.flyin.starter.gray.interceptor.GrayWebFluxFilter;
import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/** 微服务通过 网关的GlobalFilter 传递参数 */
@ConditionalOnClass(name = {"org.springframework.cloud.gateway.filter.GlobalFilter"})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnMissingBean(annotation = DisableGrayWebFluxFilter.class)
@Slf4j
public class WebFluxConfiguration {

    @Autowired private RequestRuleProperties ruleProperties;

    /*
    这个Bean不配置在GrayAutoConfiguration的原因是：
    对类 GlobalFilter 是否存在的判断， @ConditionalOnClass 放到 class 而不是 @Bean 更有效，且稳定，不易出问题。
     */
    @Bean
    public GrayWebFluxFilter grayWebFluxFilter() {
        GrayWebFluxFilter filter = new GrayWebFluxFilter();
        filter.setRuleProperties(ruleProperties);

        log.info(
                "【灰度发布处理】GrayWebFluxFilter has been initialized, you can use annotation @DisableGrayWebFluxFilter disable");
        return filter;
    }
}
