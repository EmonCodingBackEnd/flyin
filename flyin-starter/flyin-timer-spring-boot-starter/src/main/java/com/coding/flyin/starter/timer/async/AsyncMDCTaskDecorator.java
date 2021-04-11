package com.coding.flyin.starter.timer.async;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;

import java.util.Map;

public class AsyncMDCTaskDecorator implements TaskDecorator {

    @Override
    @NonNull
    public Runnable decorate(@NonNull Runnable runnable) {
        // 在同步线程中：Grab Web thread context
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                // 在异步线程中：Restore the Web thread context's MDC data
                MDC.setContextMap(contextMap);
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
