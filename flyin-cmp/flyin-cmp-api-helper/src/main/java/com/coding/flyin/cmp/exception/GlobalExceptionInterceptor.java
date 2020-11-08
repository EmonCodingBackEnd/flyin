package com.coding.flyin.cmp.exception;

import com.coding.flyin.cmp.api.AppResponse;
import com.coding.flyin.cmp.exception.annotation.DisableGlobalExceptionInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

@ControllerAdvice
@ConditionalOnMissingBean(annotation = DisableGlobalExceptionInterceptor.class)
@Slf4j
public class GlobalExceptionInterceptor {

    @PostConstruct
    public void init() {
        log.info(
                "【全局异常处理】GlobalExceptionInterceptor has been initialized, you can use annotation @DisableGlobalExceptionInterceptor disable and custom by @ControllerAdvice");
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public AppResponse handle(Exception e) {
        AppResponse<Object> appResponse = AppResponse.getDefaultResponse();
        if (e instanceof AppException) {
            AppException appException = (AppException) e;
            appResponse.setErrorCode(appException.getErrorCode());
            appResponse.setErrorMessage(appException.getErrorMessage());
        } else {
            log.error(
                    String.format(
                            "【系统异常】errorCode=%s,errorMessage=%s",
                            AppStatus.S0001.getErrorCode(), "系统意料之外的异常"),
                    e);
            appResponse.setErrorCode(AppStatus.S0001.getErrorCode());
            appResponse.setErrorMessage("系统意料之外的异常");
            appResponse.setData(e.getMessage());
        }
        return appResponse;
    }
}
