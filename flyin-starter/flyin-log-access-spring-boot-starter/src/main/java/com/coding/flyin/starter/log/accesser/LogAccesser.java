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

    default void before(HttpServletRequest request, RequestData requestData) {};

    default void after(HttpServletRequest request, HttpServletResponse response, RequestData requestData,
        ResponseData responseData) {};

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
