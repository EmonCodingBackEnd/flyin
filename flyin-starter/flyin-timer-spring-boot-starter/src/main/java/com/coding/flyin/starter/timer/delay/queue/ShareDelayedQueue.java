package com.coding.flyin.starter.timer.delay.queue;

import com.coding.flyin.starter.timer.PooledTimerTaskProperties;
import com.coding.flyin.starter.timer.delay.DelayTask;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ShareDelayedQueue {

    private final PooledTimerTaskProperties pooledTimerTaskProperties;

    private final ThreadPoolTaskExecutor delayPoolQueueExecutor;

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

    /**
     * 查看延时队列中总的延时任务数量.
     *
     * <p>创建时间: <font style="color:#00FFFF">20210724 17:26</font><br>
     * [请在此输入功能详述]
     *
     * @return java.lang.Integer - 延时任务数量
     * @author emon
     * @since 0.1.40
     */
    public static Integer size() {
        return delayedQueue.size();
    }

    /**
     * 查看延时队列中指定任务类型的延时任务数量.
     *
     * <p>创建时间: <font style="color:#00FFFF">20210724 17:26</font><br>
     * [请在此输入功能详述]
     *
     * @return java.lang.Integer - 指定任务类型的延时任务数量
     * @author emon
     * @since 0.1.40
     */
    public static <T extends DelayTask> Integer size(@NonNull Class<T> clazz) {
        return (int)
                delayedQueue.stream().filter(e -> clazz.isAssignableFrom(e.getClass())).count();
    }

    /**
     * 延时任务队列中是否存在指定延时任务.
     *
     * <p>创建时间: <font style="color:#00FFFF">20210724 17:49</font><br>
     * [请在此输入功能详述]
     *
     * @param delayTask - 指定延时任务
     * @return boolean
     * @author emon
     * @since 1.0.0
     */
    public static boolean exists(@NonNull DelayTask delayTask) {
        return delayedQueue.contains(delayTask);
    }

    public static void put(@NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        log.info("【分布式延时任务队列】任务已加入延迟队列,taskId={}", delayTask.getTaskId());
        delayedQueue.offer(delayTask, timeout, timeUnit);
    }

    /**
     * 加入延时任务到队列，如果任务尚未存在！.
     *
     * <p>创建时间: <font style="color:#00FFFF">20210721 14:52</font><br>
     * [请在此输入功能详述]
     *
     * @param delayTask - 待加入的延时任务
     * @param timeout - 延时的时间
     * @param timeUnit - 延时的时间单位
     * @return java.lang.Boolean - true-加入成功；false-已存在，不再加入
     * @author emon
     * @since 0.1.39
     */
    public static Boolean putIfAbsent(
            @NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        if (!delayedQueue.contains(delayTask)) {
            log.info("【分布式延时任务队列】任务已加入延迟队列,taskId={}", delayTask.getTaskId());
            delayedQueue.offer(delayTask, timeout, timeUnit);
            return true;
        } else {
            log.info("【分布式延时任务队列】任务已存在于延迟队列,忽略再次加入,taskId={}", delayTask.getTaskId());
            return false;
        }
    }

    public static Boolean remove(@NonNull DelayTask delayTask) {
        boolean success = delayedQueue.remove(delayTask);
        if (success) {
            log.info("【分布式延时任务队列】任务已剔除出延迟队列,taskId={}", delayTask.getTaskId());
        } else {
            log.info("【分布式延时任务队列】任务不存在于延迟队列,taskId={}", delayTask.getTaskId());
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
                "【分布式延时任务队列】任务已剔除出延迟队列,taskId={},removeCount={}",
                delayTask.getTaskId(),
                removeCount);
        return removeCount;
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
