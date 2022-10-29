package com.coding.flyin.starter.timer.delay.queue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RListMultimap;
import org.redisson.api.RedissonClient;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import com.coding.flyin.core.config.FlyinConfigurerSupport;
import com.coding.flyin.core.config.thread.ThreadContextSlot;
import com.coding.flyin.starter.timer.PooledTimerTaskProperties;
import com.coding.flyin.starter.timer.delay.DelayTask;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractShareDelayedQueue implements DelayedQueue {

    protected final PooledTimerTaskProperties.Delay delay;

    protected final ThreadPoolTaskExecutor delayPoolQueueExecutor;

    protected final RBlockingQueue<DelayTask> blockingFairQueue;

    protected final RDelayedQueue<DelayTask> delayedQueue;

    private final String logPrefix;

    // 延迟队列元素的伴生辅助对象，如果队列元素中引入了MDC，delayedQueue的contains方法便无法仅根据taskId判断元素是否存在了，利用该对象处理。
    private final RListMultimap<String, Integer> delayedQueueCompanion;

    AbstractShareDelayedQueue(PooledTimerTaskProperties.Delay delay, ThreadPoolTaskExecutor delayPoolQueueExecutor,
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
        this.delayedQueueCompanion =
            redissonClient.getListMultimap(delay.getDelayTaskQueueRedisKey().concat("companion"));
        this.init();
    }

    @Override
    public Integer size() {
        return delayedQueue.size();
    }

    @Override
    public <T extends DelayTask> Integer size(@NonNull Class<T> clazz) {
        return (int)delayedQueue.stream().filter(e -> {
            if (e == null) {
                return false;
            }
            Class<?> clz = e.getClass();
            if (DelayMDCTaskDecorator.class.isAssignableFrom(clz)) {
                clz = ((DelayMDCTaskDecorator)e).getTarget().getClass();
            }
            return clazz.isAssignableFrom(clz);
        }).count();
    }

    @Override
    public boolean exists(@NonNull DelayTask delayTask) {
        DelayMDCTaskDecorator decorate = new DelayMDCTaskDecorator(delayTask);
        // return delayedQueue.contains(decorate);
        return contains(decorate);
    }

    @Override
    public void put(@NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        DelayMDCTaskDecorator decorate = new DelayMDCTaskDecorator(delayTask);
        log.info("【{}】任务已加入延迟队列,taskId={}", logPrefix, decorate.getTaskId());
        delayedQueue.offer(decorate, timeout, timeUnit);
        put(decorate);
    }

    @Override
    public Boolean putIfAbsent(@NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit) {
        DelayMDCTaskDecorator decorate = new DelayMDCTaskDecorator(delayTask);
        // if (!delayedQueue.contains(decorate)) {
        if (!contains(decorate)) {
            log.info("【{}】任务已加入延迟队列,taskId={}", logPrefix, decorate.getTaskId());
            delayedQueue.offer(decorate, timeout, timeUnit);
            put(decorate);
            return true;
        } else {
            log.info("【{}】任务已存在于延迟队列,忽略再次加入,taskId={}", logPrefix, decorate.getTaskId());
            return false;
        }
    }

    @Override
    public Boolean remove(@NonNull DelayTask delayTask) {
        DelayMDCTaskDecorator decorate = new DelayMDCTaskDecorator(delayTask);
        boolean success = delayedQueue.remove(decorate);
        if (success) {
            remove(decorate);
            log.info("【{}】任务已剔除出延迟队列,taskId={}", logPrefix, decorate.getTaskId());
        } else {
            log.info("【{}】任务不存在于延迟队列,taskId={}", logPrefix, decorate.getTaskId());
        }
        return success;
    }

    @Override
    public Integer removeUntilNone(DelayTask delayTask) {
        DelayMDCTaskDecorator decorate = new DelayMDCTaskDecorator(delayTask);
        int removeCount = 0;
        // if (delayedQueue.contains(decorate)) {
        if (contains(decorate)) {
            boolean success;
            do {
                success = delayedQueue.remove(decorate);
                if (success) {
                    remove(decorate);
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
        log.info("【分布式{}守护线程】开启,thread={}", logPrefix, Thread.currentThread().getId());
        while (true) {
            // 阻塞式获取
            Exception exp = null;
            DelayTask target = null;
            try {
                DelayTask task = blockingFairQueue.take();
                if (task instanceof DelayMDCTaskDecorator) {
                    DelayMDCTaskDecorator decorator = (DelayMDCTaskDecorator)task;
                    target = decorator.getTarget();
                    MDC.setContextMap(decorator.getMDCEnvironment());
                    remove(decorator);
                } else {
                    target = task;
                }
                List<ThreadContextSlot> matchedSlots =
                    FlyinConfigurerSupport.getThreadContextSlot(target.getClass(), true);
                for (ThreadContextSlot threadContextSlot : matchedSlots) {
                    threadContextSlot.beforeExecute();
                }

                log.info("【分布式延时任务队列】任务已提取并加入线程池,taskId={}", task.getTaskId());
                delayPoolQueueExecutor.execute(task);
            } catch (InterruptedException e) {
                exp = e;
                log.error("【分布式延时任务队列守护线程】异常", e);
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
        log.info("【分布式{}守护线程】关闭,thread={}", logPrefix, Thread.currentThread().getId());
    }

    private void put(DelayMDCTaskDecorator decorator) {
        delayedQueueCompanion.put(decorator.getTaskIdentifier(), decorator.hashCode());
    }

    private boolean contains(DelayMDCTaskDecorator decorator) {
        return delayedQueueCompanion.containsKey(decorator.getTaskIdentifier());
    }

    private void remove(DelayMDCTaskDecorator decorator) {
        delayedQueueCompanion.remove(decorator.getTaskIdentifier(), decorator.hashCode());
    }
}
