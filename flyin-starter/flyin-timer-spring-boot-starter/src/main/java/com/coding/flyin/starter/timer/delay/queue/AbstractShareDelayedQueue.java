package com.coding.flyin.starter.timer.delay.queue;

import com.coding.flyin.starter.timer.PooledTimerTaskProperties;
import com.coding.flyin.starter.timer.delay.DelayTask;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractShareDelayedQueue implements DelayedQueue {

    protected final PooledTimerTaskProperties.Delay delay;

    protected final ThreadPoolTaskExecutor delayPoolQueueExecutor;

    protected final RBlockingQueue<DelayTask> blockingFairQueue;

    protected final RDelayedQueue<DelayTask> delayedQueue;

    private final String logPrefix;

    AbstractShareDelayedQueue(
            PooledTimerTaskProperties.Delay delay,
            ThreadPoolTaskExecutor delayPoolQueueExecutor,
            RedissonClient redissonClient) {
        this.delay = delay;
        this.delayPoolQueueExecutor = delayPoolQueueExecutor;
        this.blockingFairQueue = redissonClient.getBlockingQueue(delay.getDelayTaskQueueRedisKey());
        this.delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        String logPrefix = "分布式延时任务队列";
        if (!StringUtils.isEmpty(delay.getDelayTaskLogPrefix())) {
            logPrefix = delay.getDelayTaskLogPrefix();
        }
        this.logPrefix = logPrefix;

        this.init();
    }

    @Override
    public Integer size() {
        return delayedQueue.size();
    }

    @Override
    public <T extends DelayTask> Integer size(@NonNull Class<T> clazz) {
        return (int)
                delayedQueue.stream().filter(e -> clazz.isAssignableFrom(e.getClass())).count();
    }

    @Override
    public boolean exists(@NonNull DelayTask delayTask) {
        return delayedQueue.contains(delayTask);
    }

    @Override
    public void put(@NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        log.info("【{}】任务已加入延迟队列,taskId={}", logPrefix, delayTask.getTaskId());
        delayedQueue.offer(delayTask, timeout, timeUnit);
    }

    @Override
    public Boolean putIfAbsent(
            @NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        if (!delayedQueue.contains(delayTask)) {
            log.info("【{}】任务已加入延迟队列,taskId={}", logPrefix, delayTask.getTaskId());
            delayedQueue.offer(delayTask, timeout, timeUnit);
            return true;
        } else {
            log.info("【{}】任务已存在于延迟队列,忽略再次加入,taskId={}", logPrefix, delayTask.getTaskId());
            return false;
        }
    }

    @Override
    public Boolean remove(@NonNull DelayTask delayTask) {
        boolean success = delayedQueue.remove(delayTask);
        if (success) {
            log.info("【{}】任务已剔除出延迟队列,taskId={}", logPrefix, delayTask.getTaskId());
        } else {
            log.info("【{}】任务不存在于延迟队列,taskId={}", logPrefix, delayTask.getTaskId());
        }
        return success;
    }

    @Override
    public Integer removeUntilNone(DelayTask delayTask) {
        int removeCount = 0;
        if (delayedQueue.contains(delayTask)) {
            boolean success;
            do {
                success = delayedQueue.remove(delayTask);
                if (success) {
                    removeCount++;
                }
            } while (success);
        }
        log.info(
                "【{}】任务已剔除出延迟队列,taskId={},removeCount={}",
                logPrefix,
                delayTask.getTaskId(),
                removeCount);
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
        log.info("【分布式{}守护线程】开启,thread={}", logPrefix, Thread.currentThread().getId());
        while (true) {
            // 阻塞式获取
            try {
                DelayTask task = blockingFairQueue.take();
                log.info("【分布式延时任务队列】任务已提取并加入线程池,taskId={}", task.getTaskId());
                delayPoolQueueExecutor.execute(task);
            } catch (InterruptedException e) {
                log.error("【分布式延时任务队列守护线程】异常", e);
                break;
            }
        }
        log.info("【分布式{}守护线程】关闭,thread={}", logPrefix, Thread.currentThread().getId());
    }
}