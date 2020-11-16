package com.coding.flyin.cmp.auth.metadata.annotation.resolver;

import com.coding.flyin.cmp.auth.jwt.JwtTokenContextHolder;
import com.coding.flyin.cmp.auth.metadata.AppSession;
import com.coding.flyin.cmp.auth.metadata.annotation.AppCache;
import com.coding.flyin.cmp.exception.AppException;
import com.coding.flyin.cmp.exception.AppStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Slf4j
public class AppCacheArgumentResolver implements HandlerMethodArgumentResolver {

    // 判断是否支持要转换的参数类型
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasManageCacheAnnotation = parameter.hasParameterAnnotation(AppCache.class);
        // 自身类.class.isAssignableFrom(自身类或子类.class)  返回true
        boolean isAppSession = AppSession.class.isAssignableFrom(parameter.getParameterType());
        return hasManageCacheAnnotation && isAppSession;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws Exception {
        AppCache appCache = parameter.getParameterAnnotation(AppCache.class);
        AppSession jwtSession = JwtTokenContextHolder.getJwtTokenContext().getJwtSession();
        if (jwtSession != null) {
            // 自身类.class.isInstance(自身实例或子类实例)  返回true
            if (parameter.getParameterType().isInstance(jwtSession)) {
                return jwtSession;
            } else {
                log.error(
                        "【AppCacheArgumentResolver】登录信息类型不匹配！期望类型={}, 实际类型={}",
                        parameter.getParameterType(),
                        jwtSession);
                throw new AppException(AppStatus.U0340, "登录信息类型不匹配！");
            }
        } else {
            log.error("【AppCacheArgumentResolver】登录信息丢失或尚未登录！");
            if (Objects.requireNonNull(appCache).required()) {
                throw new AppException(AppStatus.U0230, "登录信息丢失或尚未登录！");
            }
            return null;
        }
    }
}
