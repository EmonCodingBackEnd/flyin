package com.coding.flyin.starter.timer.delay;

import org.springframework.util.Assert;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 延时处理类.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180515 13:23</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
public class DelayedItem<T extends DelayTask> implements Delayed {

    private static final AtomicLong atomicLong = new AtomicLong(0);

    /** 到期时间 */
    private final long expireTime;
    /** 任务. */
    private final T task;
    /** 当前任务编号 */
    private final long num;

    public DelayedItem(T task, long delayTime) {
        this.expireTime = System.nanoTime() + delayTime;
        this.task = task;
        this.num = atomicLong.getAndIncrement();
    }

    /**
     * 返回与此对象相关的剩余延时时间，以给定的时间单位表示.
     *
     * <p>创建时间: <font style="color:#00FFFF">20180515 13:29</font><br>
     * [请在此输入功能详述]
     *
     * @param unit -
     * @return long
     * @author Rushing0711
     * @since 0.1.0
     */
    @Override
    public long getDelay(TimeUnit unit) {
        Assert.notNull(unit, "unit must be not null");
        return unit.convert(this.expireTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        Assert.notNull(other, "delayed must be not null");
        if (other == this) // compare zero ONLY if same object
        return 0;
        if (other instanceof DelayedItem) {
            DelayedItem x = (DelayedItem) other;
            long diff = expireTime - x.expireTime;
            if (diff < 0) {
                return -1;
            } else if (diff > 0) {
                return 1;
            } else if (num < x.num) {
                return -1;
            } else {
                return 1;
            }
        }
        long d = (getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    @Override
    public int hashCode() {
        return task.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DelayedItem) {
            // return object.hashCode() == hashCode();
            String otherTaskId = ((DelayedItem) object).getTask().getTaskId();
            String taskId = this.task.getTaskId();
            return (otherTaskId == null && taskId == null)
                    || (otherTaskId != null && otherTaskId.equals(taskId));
        }
        return false;
    }

    public T getTask() {
        return this.task;
    }
}
