package com.coding.flyin.starter.gray;

import com.coding.flyin.starter.gray.annotation.DisableGrayWebMvcFilter;
import com.coding.flyin.starter.gray.interceptor.GrayWebMvcFilter;
import com.coding.flyin.starter.gray.properties.RequestRuleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 微服务通过 网关的GlobalFilter 传递参数 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnMissingBean(annotation = DisableGrayWebMvcFilter.class)
@EnableConfigurationProperties(RequestRuleProperties.class)
@Slf4j
public class GrayWebMvcAutoConfiguration {

    @Autowired private RequestRuleProperties ruleProperties;

    /*
    这个Bean不配置在GrayAutoConfiguration的原因是：
    SpringCloudGateway打包之后运行java -jar执行会导致错误：
    Caused by: java.lang.NoClassDefFoundError: javax/servlet/Filter
     */
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
}
