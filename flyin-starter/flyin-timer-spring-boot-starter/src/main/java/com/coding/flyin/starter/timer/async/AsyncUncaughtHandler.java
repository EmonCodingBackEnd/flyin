package com.coding.flyin.starter.timer.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * 异步非捕获异常处理类.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180514 18:34</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
@Slf4j
public class AsyncUncaughtHandler implements AsyncUncaughtExceptionHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SuppressWarnings("all")
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        String message =
                String.format(
                        "【异步方法执行】异常,throwable=%s, method=%s, params=%s",
                        throwable.getMessage(), method.getName(), toJsonByJackson(objects));
        log.error(message, throwable);

        // TODO: 2019/12/15 发送邮件或短信，做进一步处理
    }

    private Object toJsonByJackson(Object object) {
        String result;
        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return object;
        }
        return result;
    }
}
