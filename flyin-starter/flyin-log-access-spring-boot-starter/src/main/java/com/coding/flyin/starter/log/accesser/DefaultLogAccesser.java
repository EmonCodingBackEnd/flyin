package com.coding.flyin.starter.log.accesser;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultLogAccesser implements LogAccesser {
    @Override
    public List<String> excludePathPatterns() {
        List<String> exclude = new ArrayList<>();
        exclude.addAll(LogAccesser.swaggerUrlList);
        exclude.addAll(LogAccesser.staticUrlList);
        return exclude;
    }

    @Override
    public void before(HttpServletRequest request, RequestData requestData) {
        log.info("请求日志==>{}", requestData);
    }

    @Override
    public void after(HttpServletRequest request, HttpServletResponse response, RequestData requestData,
        LogAccesser.ResponseData responseData, Exception ex) {
        log.info(String.format("应答日志==>%s", responseData), ex);
    }
}
