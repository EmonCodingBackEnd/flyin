package com.coding.flyin.starter.log.accesser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import lombok.Builder;
import lombok.Data;

public interface LogAccesser {

    static List<String> swaggerUrlList = Arrays.asList("/doc.html", "/swagger-resources", "/v2/api-docs");
    static List<String> staticUrlList = Arrays.asList("/css/**", "/js/**", "/images/**", "/fonts/**");

    default List<String> includePathPatterns() {
        return Collections.singletonList("/**");
    }

    default List<String> excludePathPatterns() {
        return new ArrayList<>();
    }

    /**
     * 方法执行前处理
     *
     * @param request - 请求
     * @param requestData - 请求数据
     */
    default void before(HttpServletRequest request, RequestData requestData) {};

    /**
     * 方法执行后处理，无论是否异常，只要匹配到都会执行
     *
     * @param request - 请求
     * @param response - 应答
     * @param requestData - 请求数据
     * @param responseData - 应答数据
     * @param ex -
     *            未配置自定义异常处理器（ExceptionHandlerExceptionResolver/ResponseStatusExceptionResolver/XXXHandlerExceptionResolver），或者自定义异常处理器无法处理时，才会有异常
     */
    default void after(HttpServletRequest request, HttpServletResponse response, RequestData requestData,
        ResponseData responseData, Exception ex) {};

    @Data
    @Builder
    class RequestData {
        private String httpMethod;
        private String requestUri;
        /**
         * 单位：毫秒
         */
        private long requestTime;
        private String queryString;
        private String requestParameter;
        private String requestBody;
        private List<RequestFile> requestFiles;

    }

    @Data
    class RequestFile {
        private String fileKey;
        private String fileName;
        private long fileSize;
        private String fileContentType;
    }

    @Data
    @Builder
    class ResponseData {
        /**
         * 单位：毫秒
         */
        private long responseTime;
        /**
         * 单位：毫秒
         */
        private long costTime;
        private ModelAndView modelAndView;
        private String responseBody;

    }
}
