package com.coding.flyin.starter.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.coding.flyin.starter.log.accesser.DefaultLogAccesser;
import com.coding.flyin.starter.log.accesser.LogAccesser;
import com.coding.flyin.starter.log.filter.RepeatedlyFilter;
import com.coding.flyin.starter.log.interceptor.GlobalLogInterceptor;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import(GlobalLogInterceptor.class)
public class LogAutoConfiguration {

    @Bean
    public FilterRegistrationBean<RepeatedlyFilter> logFilter() {
        FilterRegistrationBean<RepeatedlyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RepeatedlyFilter());
        registrationBean.setName("RepeatedlyFilter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setEnabled(true);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 5);
        return registrationBean;
    }

    @ConditionalOnMissingBean
    @Bean
    public LogAccesser logAccesser() {
        return new DefaultLogAccesser();
    }

    @Bean
    public WebMvcConfigurer logWebMvcConfig(LogAccesser logAccesser, GlobalLogInterceptor globalLogInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(globalLogInterceptor).addPathPatterns(logAccesser.includePathPatterns())
                    .excludePathPatterns(logAccesser.excludePathPatterns());
            }
        };
    }
}
