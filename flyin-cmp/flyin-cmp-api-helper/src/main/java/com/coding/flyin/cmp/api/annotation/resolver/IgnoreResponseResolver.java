package com.coding.flyin.cmp.api.annotation.resolver;

import com.coding.flyin.cmp.api.AppResponse;
import com.coding.flyin.cmp.api.annotation.IgnoreResponse;
import com.coding.flyin.cmp.api.annotation.resolver.config.IgnoreResponseConfig;
import com.coding.flyin.cmp.api.paging.AppPagingResponse;
import com.coding.flyin.cmp.api.paging.AppPagingStandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.PostConstruct;
import java.net.URI;

/**
 * 统一响应.
 *
 * <p>创建时间: <font style="color:#00FFFF">20191130 12:02</font><br>
 * // * Deprecated：ResponseBodyAdvice是一个很鸡肋的功能，默认启用了GlobalExceptionInterceptor，废弃了这个配置；<br>
 * // * 如果需要使用请打开 @RestControllerAdvice 注解 该功能保证了Json应答的全局统一控制；对于应答AppResponse的类
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
@RestControllerAdvice
@Slf4j
public class IgnoreResponseResolver implements ResponseBodyAdvice<Object> {

    private final IgnoreResponseConfig ignoreResponseConfig;

    public IgnoreResponseResolver(IgnoreResponseConfig ignoreResponseConfig) {
        this.ignoreResponseConfig = ignoreResponseConfig;
    }

    @PostConstruct
    public void init() {
        log.info("【全局应答处理】IgnoreResponseResolver has been initialized");
    }

    /**
     * 判断是否需要对相应进行处理.
     *
     * <p>创建时间: <font style="color:#00FFFF">20191130 12:04</font><br>
     * [请在此输入功能详述]
     *
     * @param returnType -
     * @param converterType -
     * @return boolean
     * @author Rushing0711
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("all")
    public boolean supports(
            MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        // 如果包不属于管控范围，不需要处理
        String packageName = returnType.getDeclaringClass().getPackage().getName();
        if (!ignoreResponseConfig.containsPackage(packageName)) {
            return false;
        }

        // 如果不是Jackson转换器处理的，不需要处理
        boolean isJacksonConverter =
                AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
        if (!isJacksonConverter) {
            return false;
        }
        // 如果当前方法所在的类标识了 @IgnoreResponse 注解，不需要处理
        boolean classHasIgnoreResponseAnnotation =
                returnType.getDeclaringClass().isAnnotationPresent(IgnoreResponse.class);
        if (classHasIgnoreResponseAnnotation) {
            return false;
        }
        // 如果当前方法标识了 @IgnoreResponse 注解，不需要处理
        boolean methodHasIgnoreResponseAnnotation =
                returnType.getMethod().isAnnotationPresent(IgnoreResponse.class);
        if (methodHasIgnoreResponseAnnotation) {
            return false;
        }

        // 对相应进行处理，执行 beforeBodyWrite 方法
        return true;
    }

    /**
     * 相应返回之前的处理.
     *
     * <p>创建时间: <font style="color:#00FFFF">20191130 12:54</font><br>
     * [请在此输入功能详述]
     *
     * @param oriObject
     * @param returnType
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return java.lang.Object
     * @author Rushing0711
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(
            Object oriObject,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {
        // 针对SpringBoot监控接口忽略处理
        URI uri = serverHttpRequest.getURI();
        if (uri.getPath().startsWith("/actuator")) {
            return oriObject;
        }
        // 定义最终的返回对象
        AppResponse response;
        if (null == oriObject) {
            response = AppResponse.getDefaultResponse();
        } else if (oriObject instanceof AppPagingStandardResponse) {
            response = AppPagingStandardResponse.getDefaultResponse();
            BeanUtils.copyProperties(oriObject, response);
            ((AppPagingStandardResponse) response).setPaging(null);
        } else if (oriObject instanceof AppPagingResponse) {
            response = AppPagingResponse.getDefaultResponse();
            BeanUtils.copyProperties(oriObject, response);
        } else if (oriObject instanceof AppResponse) {
            response = AppResponse.getDefaultResponse();
            BeanUtils.copyProperties(oriObject, response);
        } else {
            response = AppResponse.getDefaultResponse();
            response.setData(oriObject);
        }
        return response;
    }
}
