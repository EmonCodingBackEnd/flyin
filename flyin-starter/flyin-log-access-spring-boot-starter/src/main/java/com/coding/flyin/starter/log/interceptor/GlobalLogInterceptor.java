package com.coding.flyin.starter.log.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.coding.flyin.starter.log.accesser.LogAccesser;
import com.coding.flyin.starter.log.common.LogConstants;
import com.coding.flyin.starter.log.wrapper.RepeatedlyResponseWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GlobalLogInterceptor implements HandlerInterceptor {
    private final LogAccesser logAccesser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        try {
            LogAccesser.RequestData requestData = getRequestData(request);
            logAccesser.before(request, requestData);
        } catch (IOException e) {
            log.error("parse requestData exception!", e);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        request.setAttribute(LogConstants.LOG_RES_MODEL_AND_VIEW, modelAndView);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        try {
            LogAccesser.RequestData requestData =
                (LogAccesser.RequestData)request.getAttribute(LogConstants.LOG_REQ_DATA);
            LogAccesser.ResponseData responseData = getResponseData(request, response, requestData);
            logAccesser.after(request, response, requestData, responseData, ex);
        } catch (IOException e) {
            log.error("parse responseDate exception!", e);
        }
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        StringBuilder sb = new StringBuilder();
        String requestBody;
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            sb.append(requestBody);
        }
        requestBody = sb.toString();
        return requestBody;
    }

    private String getRequestParameter(HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        StringBuilder sb = new StringBuilder();
        String requestParameter;
        Map<String, String[]> parameterMap = null;
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
            parameterMap = multipartRequest.getParameterMap();
        } else if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(contentType)) {
            parameterMap = request.getParameterMap();
        }
        if (parameterMap != null && parameterMap.size() > 0) {
            Optional<String> parameterString = parameterMap.entrySet().stream()
                .map(e -> e.getKey() + "=" + Arrays.stream(e.getValue()).reduce((v1, v2) -> v1 + "," + v2).orElse(""))
                .reduce((s1, s2) -> s1 + "&" + s2);
            sb.append(parameterString.orElse(""));
        }
        requestParameter = sb.toString();
        return requestParameter;
    }

    private List<LogAccesser.RequestFile> getRequestFile(HttpServletRequest request) throws IOException {
        List<LogAccesser.RequestFile> requestFiles = new ArrayList<>();
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
            // Collection<Part> parts = multipartRequest.getParts();
            MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
            for (Map.Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
                String fileKey = entry.getKey();
                List<MultipartFile> multipartFiles = entry.getValue();
                for (MultipartFile multipartFile : multipartFiles) {
                    String fileName = multipartFile.getOriginalFilename();
                    long fileSize = multipartFile.getSize();
                    String fileContentType = multipartFile.getContentType();

                    LogAccesser.RequestFile requestFile = new LogAccesser.RequestFile();
                    requestFile.setFileKey(fileKey);
                    requestFile.setFileName(fileName);
                    requestFile.setFileSize(fileSize);
                    requestFile.setFileContentType(fileContentType);
                    requestFiles.add(requestFile);
                }
            }
        }
        return requestFiles;
    }

    public LogAccesser.RequestData getRequestData(HttpServletRequest request) throws IOException {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String requestParameter = getRequestParameter(request);
        String requestBody = getRequestBody(request);
        List<LogAccesser.RequestFile> requestFiles = getRequestFile(request);
        LogAccesser.RequestData.RequestDataBuilder requestDataBuilder =
            LogAccesser.RequestData.builder().httpMethod(request.getMethod()).requestUri(requestURI)
                .requestTime(System.currentTimeMillis()).queryString(queryString).requestParameter(requestParameter)
                .requestBody(requestBody).requestFiles(requestFiles);

        LogAccesser.RequestData requestData = requestDataBuilder.build();
        request.setAttribute(LogConstants.LOG_REQ_DATA, requestData);
        return requestData;
    }

    private String getResponseBody(HttpServletResponse response) throws IOException {
        String contentType = response.getContentType();
        StringBuilder sb = new StringBuilder();
        String responseBody;
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            // 避免内部错误 /error 时，response并不是RepeatedlyResponseWrapper类型
            if (response instanceof RepeatedlyResponseWrapper) {
                responseBody =
                    new String(((RepeatedlyResponseWrapper)response).getResponseData(), StandardCharsets.UTF_8);
                sb.append(responseBody);
            }
        }
        responseBody = sb.toString();
        return responseBody;
    }

    public LogAccesser.ResponseData getResponseData(HttpServletRequest request, HttpServletResponse response,
        LogAccesser.RequestData requestData) throws IOException {
        long beginTime = requestData.getRequestTime();
        long endTime = System.currentTimeMillis();
        ModelAndView modelAndView = (ModelAndView)request.getAttribute(LogConstants.LOG_RES_MODEL_AND_VIEW);
        String responseBody = getResponseBody(response);
        LogAccesser.ResponseData.ResponseDataBuilder requestDataBuilder =
            LogAccesser.ResponseData.builder().responseTime(System.currentTimeMillis()).costTime(endTime - beginTime)
                .modelAndView(modelAndView).responseBody(responseBody);
        return requestDataBuilder.build();
    }
}
