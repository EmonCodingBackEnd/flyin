package com.coding.flyin.starter.log.accesser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

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

    @Getter
    @Builder
    class RequestData {
        /**
         * 单位：毫秒
         */
        private long requestTime;
        private String queryString;
        private String requestUri;
        private String requestParameter;
        private String requestBody;
        private List<RequestFile> requestFiles;

        @Override
        public String toString() {
            return "RequestData{" + "requestTime=" + requestTime + ", queryString='" + queryString + '\''
                + ", requestUri='" + requestUri + '\'' + ", requestParameter='" + requestParameter + '\''
                + ", requestBody='" + requestBody + '\'' + ", requestFiles=" + requestFiles + '}';
        }
    }

    @Data
    class RequestFile {
        private String fileKey;
        private String fileName;
        private long fileSize;
        private String fileContentType;
    }

    @Getter
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
        private String responseBody;

        @Override
        public String toString() {
            return "ResponseData{" + "responseTime=" + responseTime + ", costTime=" + costTime + ", responseBody='"
                + responseBody + '\'' + '}';
        }
    }
}
