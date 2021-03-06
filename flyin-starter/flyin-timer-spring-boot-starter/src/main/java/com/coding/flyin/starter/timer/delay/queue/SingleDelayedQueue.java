package com.coding.flyin.starter.timer.delay.queue;

import com.coding.flyin.starter.timer.PooledTimerTaskProperties;
import com.coding.flyin.starter.timer.delay.DelayTask;
import com.coding.flyin.starter.timer.delay.DelayedItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列守护线程.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180515 14:11</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
@Slf4j
public class SingleDelayedQueue {

    private PooledTimerTaskProperties pooledTimerTaskProperties;

    private ThreadPoolTaskExecutor delayPoolQueueExecutor;

    public SingleDelayedQueue(
            PooledTimerTaskProperties pooledTimerTaskProperties,
            ThreadPoolTaskExecutor delayPoolQueueExecutor) {
        this.pooledTimerTaskProperties = pooledTimerTaskProperties;
        this.delayPoolQueueExecutor = delayPoolQueueExecutor;
    }

    /** 创建一个最初为空的新 DelayQueue */
    private static DelayQueue<DelayedItem> delayedItems = new DelayQueue<>();

    private DelayQueue<DelayedItem> getDelayedItems() {
        return delayedItems;
    }

    /**
     * 添加任务到延时队列.
     *
     * <p>创建时间: <font style="color:#00FFFF">20180515 14:06</font><br>
     * [请在此输入功能详述]
     *
     * @param delayTask - 延时任务，继承Thread类或实现Runnable接口的类
     * @param timeout - 延时时间，单位毫秒
     * @author Rushing0711
     * @since 0.1.0
     */
    public static void put(DelayTask delayTask, long timeout, TimeUnit timeUnit) {
        log.info("【单机版延时任务队列】任务已加入延迟队列,taskId={}", delayTask.getTaskId());
        long nanoTime = TimeUnit.NANOSECONDS.convert(timeout, timeUnit);
        // 创建一个任务
        DelayedItem<DelayTask> delayedItem = new DelayedItem<>(delayTask, nanoTime);
        // 阻塞式将任务放在延时的队列中
        delayedItems.put(delayedItem);
    }

    public static Boolean remove(DelayTask delayTask) {
        // 创建一个任务
        DelayedItem<DelayTask> delayedItem = new DelayedItem<>(delayTask, 0);
        boolean success = delayedItems.remove(delayedItem);
        if (success) {
            log.info("【单机版延时任务队列】任务已剔除出延迟队列,taskId={}", delayTask.getTaskId());
        } else {
            log.info("【单机版延时任务队列】任务不存在于延迟队列,taskId={}", delayTask.getTaskId());
        }
        return success;
    }

    /**
     * 剔除出指定延时任务，直到全部都被剔除出.
     *
     * <p>创建时间: <font style="color:#00FFFF">20210720 15:51</font><br>
     * [请在此输入功能详述]
     *
     * @param delayTask - 指定延时任务
     * @return java.lang.Integer - 剔除出指定延时任务的数量
     * @author emon
     * @since 0.1.38
     */
    public static Integer removeUntilNone(DelayTask delayTask) {
        // 创建一个任务
        DelayedItem<DelayTask> delayedItem = new DelayedItem<>(delayTask, 0);
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
        log.info(
                "【单机版延时任务队列】任务已剔除出延迟队列,taskId={},removeCount={}",
                delayTask.getTaskId(),
                removeCount);
        return removeCount;
    }

    /** 初始化守护线程 */
    @PostConstruct
    private void init() {
        log.info("【初始化单机版延时任务队列守护线程】开始......");
        Thread daemonThread = new Thread(this::execute);
        daemonThread.setDaemon(true);
        daemonThread.setName(
                pooledTimerTaskProperties.getDelay().getDelayTaskQueueDaemonThreadName());
        daemonThread.start();
        log.info("【初始化单机版延时任务队列守护线程】完成......");
    }

    private void execute() {
        log.info("【单机版延时任务队列守护线程】开启,thread={}", Thread.currentThread().getId());
        while (true) {
            // 阻塞式获取
            DelayedItem item;
            try {
                item = getDelayedItems().take();
                DelayTask task = item.getTask();
                if (task == null) {
                    continue;
                }
                log.info("【单机版延时任务队列】任务已提取并加入线程池,taskId={}", task.getTaskId());
                delayPoolQueueExecutor.execute(task);
            } catch (InterruptedException e) {
                log.error("【单机版延时任务队列守护线程】异常", e);
                break;
            }
        }
        log.info("【单机版延时任务队列守护线程】关闭,thread={}", Thread.currentThread().getId());
    }
}
