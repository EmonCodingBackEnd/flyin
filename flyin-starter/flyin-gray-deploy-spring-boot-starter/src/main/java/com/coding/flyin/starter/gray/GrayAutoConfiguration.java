package com.coding.flyin.starter.gray;

import com.coding.flyin.starter.gray.annotation.EnableMvcGrayFilter;
import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import com.coding.flyin.starter.gray.filter.MvcGrayFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@Import({GrayAutoConfiguration.class, FeignConfiguration.class})
public class GrayAutoConfiguration {

    @Autowired private RequestRuleProperties ruleProperties;

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(coreHttpRequestInterceptor());
        return restTemplate;
    }

    @Bean
    public GrayHttpRequestInterceptor coreHttpRequestInterceptor() {
        GrayHttpRequestInterceptor interceptor = new GrayHttpRequestInterceptor();
        interceptor.setRuleProperties(ruleProperties);
        return interceptor;
    }

    @Bean
    @ConditionalOnBean(annotation = EnableMvcGrayFilter.class)
    public FilterRegistrationBean<MvcGrayFilter> mvcGrayFilterRegistration() {
        FilterRegistrationBean<MvcGrayFilter> registrationBean = new FilterRegistrationBean<>();
        MvcGrayFilter filter = new MvcGrayFilter();
        filter.setRuleProperties(ruleProperties);
        registrationBean.setFilter(filter);
        registrationBean.setName("mvcGrayFilter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setEnabled(true);
        registrationBean.setOrder(Integer.MIN_VALUE + 10);
        return registrationBean;
    }
}
