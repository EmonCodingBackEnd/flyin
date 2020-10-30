package com.coding.flyin.starter.timer.delay.queue;

import com.coding.flyin.starter.timer.PooledTimerTaskProperties;
import com.coding.flyin.starter.timer.delay.DelayTask;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ShareDelayedQueue {

    private PooledTimerTaskProperties pooledTimerTaskProperties;

    private ThreadPoolTaskExecutor delayPoolQueueExecutor;

    public ShareDelayedQueue(
            PooledTimerTaskProperties pooledTimerTaskProperties,
            ThreadPoolTaskExecutor delayPoolQueueExecutor,
            RedissonClient redissonClient) {
        this.pooledTimerTaskProperties = pooledTimerTaskProperties;
        this.delayPoolQueueExecutor = delayPoolQueueExecutor;

        blockingFairQueue =
                redissonClient.getBlockingQueue(
                        pooledTimerTaskProperties.getDelay().getDelayTaskQueueRedisKey());
        delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
    }

    private static RBlockingQueue<DelayTask> blockingFairQueue;
    private static RDelayedQueue<DelayTask> delayedQueue;

    public static void put(DelayTask delayTask, long timeout, TimeUnit timeUnit) {
        log.info("【延时任务队列】任务已加入延迟队列,taskId={}", delayTask.getTaskId());
        delayedQueue.offer(delayTask, timeout, timeUnit);
    }

    @PostConstruct
    private void init() {
        log.info("【初始化分布式延时任务队列守护线程】开始......");
        Thread daemonThread = new Thread(this::execute);
        daemonThread.setDaemon(true);
        daemonThread.setName(
                pooledTimerTaskProperties.getDelay().getDelayTaskQueueDaemonThreadName());
        daemonThread.start();
        log.info("【初始化分布式延时任务队列守护线程】完成......");
    }

    private void execute() {
        log.info("【分布式延时任务队列守护线程】开启,thread={}", Thread.currentThread().getId());
        while (true) {
            // 阻塞式获取
            try {
                DelayTask task = blockingFairQueue.take();
                if (task == null) {
                    continue;
                }
                log.info("【分布式延时任务队列】任务已提取并加入线程池,taskId={}", task.getTaskId());
                delayPoolQueueExecutor.execute(task);
            } catch (InterruptedException e) {
                log.error("【分布式延时任务队列守护线程】异常", e);
                break;
            }
        }
        log.info("【分布式延时任务队列守护线程】关闭,thread={}", Thread.currentThread().getId());
    }
}
