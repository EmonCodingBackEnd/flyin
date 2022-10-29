package com.coding.flyin.starter.timer.async;

import java.util.List;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;

import com.coding.flyin.core.config.FlyinConfigurerSupport;
import com.coding.flyin.core.config.thread.ThreadContextSlot;

public class AsyncMDCTaskDecorator implements TaskDecorator {

    @Override
    @NonNull
    public Runnable decorate(@NonNull Runnable runnable) {
        // 在同步线程中：Grab Web thread context
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            Exception exp = null;
            try {
                // 在异步线程中：Restore the Web thread context's MDC data
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                List<ThreadContextSlot> matchedSlots =
                    FlyinConfigurerSupport.getThreadContextSlot(runnable.getClass(), true);
                for (ThreadContextSlot threadContextSlot : matchedSlots) {
                    threadContextSlot.beforeRun();
                }
                runnable.run();
            } catch (Exception e) {
                exp = e;
                throw e;
            } finally {
                if (contextMap != null) {
                    MDC.clear();
                }
                List<ThreadContextSlot> matchedSlots =
                    FlyinConfigurerSupport.getThreadContextSlot(runnable.getClass(), false);
                for (ThreadContextSlot threadContextSlot : matchedSlots) {
                    threadContextSlot.afterRun(exp);
                }
            }
        };
    }
}
