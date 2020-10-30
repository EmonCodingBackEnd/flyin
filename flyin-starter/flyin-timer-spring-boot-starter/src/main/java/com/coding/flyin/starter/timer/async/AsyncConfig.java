package com.coding.flyin.starter.timer.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;

/**
 * 异步任务配置.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180514 16:37</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    private Executor asyncPoolExecutor;

    public AsyncConfig(Executor asyncPoolExecutor) {
        this.asyncPoolExecutor = asyncPoolExecutor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return asyncPoolExecutor;
    }

    /** 注意：该异常拦截器，仅针对无返回结果的异步方法进行拦截；如果是有返回值的异步方法，请使用 Future<T>.get() 并捕获异常. */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtHandler();
    }
}
