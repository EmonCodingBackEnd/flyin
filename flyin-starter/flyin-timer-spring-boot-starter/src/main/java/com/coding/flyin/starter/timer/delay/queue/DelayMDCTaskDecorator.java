package com.coding.flyin.starter.timer.delay.queue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.MDC;

import com.coding.flyin.core.config.FlyinConfigurerSupport;
import com.coding.flyin.core.config.thread.ThreadContextSlot;
import com.coding.flyin.starter.timer.delay.DelayTask;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
class DelayMDCTaskDecorator implements DelayTask {

    private static final long serialVersionUID = 3495188070378210952L;

    @lombok.NonNull
    private DelayTask target;

    private Map<String, String> MDCEnvironment = null;

    public DelayMDCTaskDecorator(@lombok.NonNull DelayTask target) {
        this.target = target;

        // 在同步线程中：Grab Web thread context
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        if (contextMap != null) {
            // 去掉key或value为null的元素，避免序列化对key=null不支持而报错，并避免因为反序列化会丢失value=null的值，而导致hashcode不一致的问题。
            contextMap = contextMap.entrySet().stream().filter(e -> e.getKey() != null && e.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            this.MDCEnvironment = new HashMap<>(contextMap);
        }
    }

    @Override
    public String getTaskId() {
        return target.getTaskId();
    }

    public String getTaskIdentifier() {
        return String.format("%s[%s]", target.getClass().getName(), getTaskId());
    }

    @Override
    public void run() {
        Exception exp = null;
        try {
            // 在延迟任务线程中：Restore the Web thread context's MDC data
            if (MDCEnvironment != null) {
                MDC.setContextMap(MDCEnvironment);
            }
            List<ThreadContextSlot> matchedSlots = FlyinConfigurerSupport.getThreadContextSlot(target.getClass(), true);
            for (ThreadContextSlot threadContextSlot : matchedSlots) {
                threadContextSlot.beforeRun();
            }
            target.run();
        } catch (Exception e) {
            exp = e;
            throw e;
        } finally {
            if (MDCEnvironment != null) {
                MDC.clear();
            }
            List<ThreadContextSlot> matchedSlots =
                FlyinConfigurerSupport.getThreadContextSlot(target.getClass(), false);
            for (ThreadContextSlot threadContextSlot : matchedSlots) {
                threadContextSlot.afterRun(exp);
            }
        }
    }

    // 仅针对 AbstractSingleDelayedQueue 根据 taskId 来判断是否同一个延迟任务；对 AbstractShareDelayedQueue
    // 不起作用！后者使用redis+lua脚本判断，需要额外的辅助，才能在保存MDC情况下判定是否同一个对象时忽略MDC的影响。
    // ==================================================华丽的分割线==================================================
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DelayMDCTaskDecorator decorator = (DelayMDCTaskDecorator)o;
        return target.getTaskId().equals(decorator.target.getTaskId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(target.getTaskId(), MDCEnvironment);
    }

    @Override
    public String toString() {
        return "DelayMDCTaskDecorator{" + "taskId=" + target.getTaskId() + ", MDCEnvironment=" + MDCEnvironment + '}';
    }
    // ==================================================华丽的分割线==================================================
}