package com.coding.flyin.starter.gray;

import com.coding.flyin.starter.gray.annotation.DisableMvcGrayFilter;
import com.coding.flyin.starter.gray.filter.MvcGrayFilter;
import com.coding.flyin.starter.gray.interceptor.GrayHttpRequestInterceptor;
import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
@RibbonClients(defaultConfiguration = DefaultRibbonConfiguration.class)
@EnableConfigurationProperties(RequestRuleProperties.class)
@ConditionalOnMissingBean(annotation = DisableMvcGrayFilter.class)
@Import({FeignConfiguration.class})
@Slf4j
public class GrayAutoConfiguration {;
    @Autowired private RequestRuleProperties ruleProperties;

    @PostConstruct
    public void init() {
        log.info(
                "【灰度发布过滤器】MvcGrayFilter has been initialized, you can use annotation @DisableMvcGrayFilter disable");
    }

    /** 微服务之间通过Feign调用时拦截并加入x-rule规则. */
    @LoadBalanced
    @Bean
    RestTemplate restTemplateForFeign() {
        GrayHttpRequestInterceptor interceptor = new GrayHttpRequestInterceptor();
        interceptor.setRuleProperties(ruleProperties);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(interceptor);
        return restTemplate;
    }

    /** 为Web服务添加过滤器. */
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
}
