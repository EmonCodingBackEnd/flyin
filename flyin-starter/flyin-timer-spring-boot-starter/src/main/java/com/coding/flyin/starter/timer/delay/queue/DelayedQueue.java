package com.coding.flyin.starter.timer.delay.queue;

import com.coding.flyin.starter.timer.delay.DelayTask;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * 延时任务队列.
 *
 * <p>创建时间: <font style="color:#00FFFF">20210725 09:10</font><br>
 * [请在此输入功能详述]
 *
 * @author emon
 * @version 0.1.40
 * @since 0.1.40
 */
public interface DelayedQueue {

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
    Integer size();

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
    <T extends DelayTask> Integer size(@NonNull Class<T> clazz);

    /**
     * 延时任务队列中是否存在指定延时任务.
     *
     * <p>创建时间: <font style="color:#00FFFF">20210724 17:49</font><br>
     * [请在此输入功能详述]
     *
     * @param delayTask - 指定延时任务
     * @return boolean
     * @author emon
     * @since 0.1.40
     */
    boolean exists(@NonNull DelayTask delayTask);

    /**
     * 添加任务到延时队列.
     *
     * <p>创建时间: <font style="color:#00FFFF">20180515 14:06</font><br>
     * [请在此输入功能详述]
     *
     * @param delayTask - 延时任务，继承Thread类或实现Runnable接口的类
     * @param timeout - 延时时间，单位毫秒
     * @author Rushing0711
     * @since 0.1.40
     */
    void put(@NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit);

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
     * @since 0.1.40
     */
    Boolean putIfAbsent(@NonNull DelayTask delayTask, long timeout, @NonNull TimeUnit timeUnit);

    /**
     * 剔除出指定延时任务一次.
     *
     * <p>创建时间: <font style="color:#00FFFF">20210724 23:03</font><br>
     * [请在此输入功能详述]
     *
     * @param delayTask - 指定延时任务
     * @return java.lang.Boolean - true-剔除成功；false-剔除失败
     * @author emon
     * @since 0.1.40
     */
    Boolean remove(@NonNull DelayTask delayTask);

    /**
     * 剔除出指定延时任务，直到全部都被剔除出.
     *
     * <p>创建时间: <font style="color:#00FFFF">20210720 15:51</font><br>
     * [请在此输入功能详述]
     *
     * @param delayTask - 指定延时任务
     * @return java.lang.Integer - 剔除出指定延时任务的数量
     * @author emon
     * @since 0.1.40
     */
    Integer removeUntilNone(DelayTask delayTask);
}
