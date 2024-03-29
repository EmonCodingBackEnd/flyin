package com.coding.flyin.starter.timer.delay.queue;

import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import com.coding.flyin.core.config.FlyinConfigurerSupport;
import com.coding.flyin.core.config.thread.ThreadContextSlot;
import com.coding.flyin.starter.timer.PooledTimerTaskProperties;
import com.coding.flyin.starter.timer.delay.DelayTask;
import com.coding.flyin.starter.timer.delay.DelayedItem;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 延时队列守护线程.
 *
 * <p>
 * 创建时间: <font style="color:#00FFFF">20180515 14:11</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
@Slf4j
public abstract class AbstractSingleDelayedQueue implements DelayedQueue {

    private final PooledTimerTaskProperties.Delay delay;

    private final ThreadPoolTaskExecutor delayPoolQueueExecutor;

    /** 创建一个最初为空的新 DelayQueue */
    private final DelayQueue<DelayedItem<?>> delayedItems = new DelayQueue<>();

    private final String logPrefix;

    public AbstractSingleDelayedQueue(PooledTimerTaskProperties.Delay delay,
        ThreadPoolTaskExecutor delayPoolQueueExecutor) {
        this.delay = delay;
        this.delayPoolQueueExecutor = delayPoolQueueExecutor;

        String logPrefix = "单机版延时任务队列";
        if (!StringUtils.isEmpty(delay.getDelayTaskLogPrefix())) {
            logPrefix = delay.getDelayTaskLogPrefix();
        }
        this.logPrefix = logPrefix;

        this.init();
    }

    private DelayQueue<DelayedItem<?>> getDelayedItems() {
        return delayedItems;
    }

    @Override
    public Integer size() {
        return delayedItems.size();
    }

    @Override
    public <T extends DelayTask> Integer size(@NonNull Class<T> clazz) {
        return (int)delayedItems.stream().filter(e -> {
            DelayTask delayTask = e.getTask();
            if (delayTask == null) {
                return false;
            }
            Class<?> clz = delayTask.getClass();
            if (DelayMDCTaskDecorator.class.isAssignableFrom(clz)) {
                clz = ((DelayMDCTaskDecorator)delayTask).getTarget().getClass();
            }
            return clazz.isAssignableFrom(clz);
        }).count();
    }

    @Override
    public boolean exists(@NonNull DelayTask delayTask) {
        // 创建一个任务
        DelayMDCTaskDecorator decorate = new DelayMDCTaskDecorator(delayTask);
        DelayedItem<DelayTask> delayedItem = new DelayedItem<>(decorate, 1);
        return delayedItems.contains(delayedItem);
    }

    public void put(@NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        DelayMDCTaskDecorator decorate = new DelayMDCTaskDecorator(delayTask);
        log.info("【{}】任务已加入延迟队列,taskId={}", logPrefix, decorate.getTaskId());
        long nanoTime = TimeUnit.NANOSECONDS.convert(timeout, timeUnit);
        // 创建一个任务
        DelayedItem<DelayTask> delayedItem = new DelayedItem<>(decorate, nanoTime);
        // 阻塞式将任务放在延时的队列中
        delayedItems.put(delayedItem);
    }

    public Boolean putIfAbsent(@NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        DelayMDCTaskDecorator decorate = new DelayMDCTaskDecorator(delayTask);
        long nanoTime = TimeUnit.NANOSECONDS.convert(timeout, timeUnit);
        // 创建一个任务
        DelayedItem<DelayTask> delayedItem = new DelayedItem<>(decorate, nanoTime);
        if (!delayedItems.contains(delayedItem)) {
            log.info("【{}】任务已加入延迟队列,taskId={}", logPrefix, decorate.getTaskId());
            delayedItems.put(delayedItem);
            return true;
        } else {
            log.info("【{}】任务已存在于延迟队列,忽略再次加入,taskId={}", logPrefix, decorate.getTaskId());
            return false;
        }
    }

    public Boolean remove(@NonNull DelayTask delayTask) {
        DelayMDCTaskDecorator decorate = new DelayMDCTaskDecorator(delayTask);
        // 创建一个任务
        DelayedItem<DelayTask> delayedItem = new DelayedItem<>(decorate, 0);
        boolean success = delayedItems.remove(delayedItem);
        if (success) {
            log.info("【{}】任务已剔除出延迟队列,taskId={}", logPrefix, decorate.getTaskId());
        } else {
            log.info("【{}】任务不存在于延迟队列,taskId={}", logPrefix, decorate.getTaskId());
        }
        return success;
    }

    public Integer removeUntilNone(@NonNull DelayTask delayTask) {
        DelayMDCTaskDecorator decorate = new DelayMDCTaskDecorator(delayTask);
        // 创建一个任务
        DelayedItem<DelayTask> delayedItem = new DelayedItem<>(decorate, 0);
        int removeCount = 0;
        if (delayedItems.contains(delayedItem)) {
            boolean success;
            do {
                success = delayedItems.remove(delayedItem);
                if (success) {
                    removeCount++;
                }
            } while (success);
        }
        log.info("【{}】任务已剔除出延迟队列,taskId={},removeCount={}", logPrefix, decorate.getTaskId(), removeCount);
        return removeCount;
    }

    private void init() {
        log.info("【初始化{}守护线程】开始......", logPrefix);
        Thread daemonThread = new Thread(this::execute);
        daemonThread.setDaemon(true);
        daemonThread.setName(delay.getDelayTaskQueueDaemonThreadName());
        daemonThread.start();
        log.info("【初始化{}守护线程】完成......", logPrefix);
    }

    private void execute() {
        log.info("【{}守护线程】开启,thread={}", logPrefix, Thread.currentThread().getId());
        while (true) {
            // 阻塞式获取
            Exception exp = null;
            DelayTask target = null;
            DelayedItem<?> item;
            try {
                item = getDelayedItems().take();
                DelayTask task = item.getTask();
                if (task == null) {
                    continue;
                }
                if (task instanceof DelayMDCTaskDecorator) {
                    DelayMDCTaskDecorator decorator = (DelayMDCTaskDecorator)task;
                    target = decorator.getTarget();
                    MDC.setContextMap(decorator.getMDCEnvironment());
                } else {
                    target = task;
                }
                List<ThreadContextSlot> matchedSlots =
                    FlyinConfigurerSupport.getThreadContextSlot(target.getClass(), true);
                for (ThreadContextSlot threadContextSlot : matchedSlots) {
                    threadContextSlot.beforeExecute();
                }

                log.info("【{}】任务已提取并加入线程池,taskId={}", logPrefix, task.getTaskId());
                delayPoolQueueExecutor.execute(task);
                if (task instanceof DelayMDCTaskDecorator) {
                    DelayMDCTaskDecorator decorator = (DelayMDCTaskDecorator)task;
                    remove(decorator);
                }
            } catch (InterruptedException e) {
                exp = e;
                log.error(String.format("【%s守护线程】异常", logPrefix), e);
                break;
            } finally {
                MDC.clear();
                if (target != null) {
                    List<ThreadContextSlot> matchedSlots =
                        FlyinConfigurerSupport.getThreadContextSlot(target.getClass(), false);
                    for (ThreadContextSlot threadContextSlot : matchedSlots) {
                        threadContextSlot.afterExecute(exp);
                    }
                }
            }
        }
        log.info("【{}守护线程】关闭,thread={}", logPrefix, Thread.currentThread().getId());
    }
}
