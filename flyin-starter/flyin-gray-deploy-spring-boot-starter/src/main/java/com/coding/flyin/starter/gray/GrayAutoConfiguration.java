package com.coding.flyin.starter.gray;

import com.coding.flyin.starter.gray.annotation.DisableGrayWebMvcFilter;
import com.coding.flyin.starter.gray.configuration.FeignConfiguration;
import com.coding.flyin.starter.gray.configuration.WebFluxConfiguration;
import com.coding.flyin.starter.gray.interceptor.GrayHttpRequestInterceptor;
import com.coding.flyin.starter.gray.interceptor.GrayWebMvcFilter;
import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import com.coding.flyin.starter.gray.rule.ribbon.DefaultRibbonConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Configuration
@RibbonClients(defaultConfiguration = DefaultRibbonConfiguration.class)
@EnableConfigurationProperties(RequestRuleProperties.class)
@Import({FeignConfiguration.class, WebFluxConfiguration.class})
@Slf4j
public class GrayAutoConfiguration {

    @Autowired private RequestRuleProperties ruleProperties;

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean(annotation = DisableGrayWebMvcFilter.class)
    @Bean
    /*
       @ConditionalOnBean(annotation = EnableGrayWebMvcFilter.class) 无法生效，参见 https://github.com/spring-projects/spring-boot/issues/15177
       替换成 @ConditionalOnBean(value = RequestRuleProperties.class, annotation = EnableMvcGrayFilter.class) 才生效，
       但这种方式看不惯，决定使用 @ConditionalOnMissingBean(annotation = DisableGrayWebMvcFilter.class) 替代，采用默认生效的方式
    */
    public FilterRegistrationBean<GrayWebMvcFilter> grayWebMvcFilterRegistration() {
        FilterRegistrationBean<GrayWebMvcFilter> registrationBean = new FilterRegistrationBean<>();
        GrayWebMvcFilter filter = new GrayWebMvcFilter();
        filter.setRuleProperties(ruleProperties);
        registrationBean.setFilter(filter);
        registrationBean.setName("grayWebMvcFilter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setEnabled(true);
        registrationBean.setOrder(Integer.MIN_VALUE + 10);

        log.info(
                "【灰度发布处理】GrayWebMvcFilter has been initialized, you can use annotation @DisableGrayWebMvcFilter disable");
        return registrationBean;
    }

    /** 微服务通过 ClientHttpRequestInterceptor 传递参数 */
    @LoadBalanced
    @Bean
    RestTemplate restTemplateForFeign() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new GrayHttpRequestInterceptor());
        return restTemplate;
    }
}
