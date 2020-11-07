package com.coding.flyin.cmp.api.annotation.resolver;

import com.coding.flyin.cmp.api.AppResponse;
import com.coding.flyin.cmp.api.annotation.IgnoreResponse;
import com.coding.flyin.cmp.api.paging.AppPagingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.PostConstruct;

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
     * @param methodParameter -
     * @param aClass -
     * @return boolean
     * @author Rushing0711
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("all")
    public boolean supports(
            MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果当前方法所在的类标识了 @IgnoreResponse 注解，不需要处理
        boolean classHasIgnoreResponseAnnotation =
                methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponse.class);
        if (classHasIgnoreResponseAnnotation) {
            return false;
        }
        // 如果当前方法标识了 @IgnoreResponse 注解，不需要处理
        boolean methodHasIgnoreResponseAnnotation =
                methodParameter.getMethod().isAnnotationPresent(IgnoreResponse.class);
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
     * @param o
     * @param methodParameter
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
            MethodParameter methodParameter,
            MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass,
            ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {
        // 定义最终的返回对象
        AppResponse<Object> response;
        if (null == oriObject) {
            response = AppResponse.getDefaultResponse();
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
