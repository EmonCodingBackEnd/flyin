package com.coding.flyin.starter.timer.delay.queue;

import com.coding.flyin.starter.timer.delay.DelayTask;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
class DelayMDCTaskDecorator implements DelayTask {

    private static final long serialVersionUID = 3495188070378210952L;

    @NonNull private DelayTask target;

    private Map<String, String> MDCEnvironment = null;

    public DelayMDCTaskDecorator(@NonNull DelayTask target) {
        this.target = target;

        // 针对 AbstractShareDelayedQueue，如果引入了MDC会导致contains方法失效，故而忽略掉！
        /*// 在同步线程中：Grab Web thread context
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        if (contextMap != null && contextMap.containsKey(GlobalConstants.TRACE_ID)) {
            this.MDCEnvironment = new HashMap<>();
            this.MDCEnvironment.put(
                    GlobalConstants.TRACE_ID, contextMap.get(GlobalConstants.TRACE_ID));
        }*/
    }

    @Override
    public String getTaskId() {
        return target.getTaskId();
    }

    @Override
    public void run() {
        try {
            // 在延迟任务线程中：Restore the Web thread context's MDC data
            if (MDCEnvironment != null) {
                MDC.setContextMap(MDCEnvironment);
            }
            target.run();
        } finally {
            if (MDCEnvironment != null) {
                MDC.clear();
            }
        }
    }

    // 仅针对 AbstractSingleDelayedQueue 根据 taskId 来判断是否同一个延迟任务；对 AbstractShareDelayedQueue
    // 不起作用！后者使用redis+lua脚本判断
    // ==================================================华丽的分割线==================================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DelayMDCTaskDecorator that = (DelayMDCTaskDecorator) o;
        return Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(target);
    }

    @Override
    public String toString() {
        return "DelayMDCTaskDecorator{" + "target=" + target + '}';
    }
    // ==================================================华丽的分割线==================================================
}
