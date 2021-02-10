package com.coding.flyin.starter.gray;

import com.coding.flyin.starter.gray.annotation.DisableMvcGrayFilter;
import com.coding.flyin.starter.gray.interceptor.MvcGrayFilter;
import com.coding.flyin.starter.gray.interceptor.FeignHeaderRequestInterceptor;
import com.coding.flyin.starter.gray.interceptor.GrayHttpRequestInterceptor;
import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import com.coding.flyin.starter.gray.rule.ribbon.DefaultRibbonConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
@RibbonClients(defaultConfiguration = DefaultRibbonConfiguration.class)
@EnableConfigurationProperties(RequestRuleProperties.class)
@ConditionalOnMissingBean(annotation = DisableMvcGrayFilter.class)
@Slf4j
public class GrayAutoConfiguration {

    @Autowired private RequestRuleProperties ruleProperties;

    @PostConstruct
    public void init() {
        log.info(
                "【灰度发布处理】MvcGrayFilter has been initialized, you can use annotation @DisableMvcGrayFilter disable");
    }

    /** 为Web服务添加过滤器，解析出某一个请求在该服务中的有效label. */
    @Bean
    /*
       @ConditionalOnBean(annotation = EnableMvcGrayFilter.class) 无法生效，参见 https://github.com/spring-projects/spring-boot/issues/15177
       替换成 @ConditionalOnBean(value = RequestRuleProperties.class, annotation = EnableMvcGrayFilter.class) 才生效，
       但这种方式看不惯，决定使用 @ConditionalOnMissingBean(annotation = EnableMvcGrayFilter.class) 替代，采用默认生效的方式
    */
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

    /** 微服务通过 feign.RequestInterceptor 传递参数 */
    @ConditionalOnClass(name = {"feign.RequestInterceptor"})
    @RefreshScope
    @Bean
    public FeignHeaderRequestInterceptor feignHeaderRequestInterceptor() {
        return new FeignHeaderRequestInterceptor();
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
