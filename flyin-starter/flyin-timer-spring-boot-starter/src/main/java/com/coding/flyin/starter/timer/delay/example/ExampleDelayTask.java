package com.coding.flyin.starter.timer.delay.example;

import com.coding.flyin.starter.timer.delay.DelayRetryTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 演示版延时任务，如有需要，请拷贝到具体微服务修改.
 *
 * <p>
 *
 * <p>创建时间: <font style="color:#00FFFF">20180515 14:22</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
// @Component
@Slf4j
public class ExampleDelayTask extends DelayRetryTask implements ApplicationContextAware {

    private static final long serialVersionUID = -9157077217216143833L;

    private Long orderId;

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ExampleDelayTask.applicationContext == null) {
            ExampleDelayTask.applicationContext = applicationContext;
        }
    }

    // 定义私有构造器，防止被外部初始化
    private ExampleDelayTask() {}

    public ExampleDelayTask(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public void run() {
        try {
            log.info("【延迟任务】ExampleDelayTask执行完毕！");
        } catch (Exception e) {
            log.error(String.format("【延时任务】执行异常,taskId=%s", this.getTaskId()), e);
            if (canIRetry()) {
                this.retry();
                log.info("【延时任务】重试次数={},taskId={}", this.getRetryTimes(), this.getTaskId());
            } else {
                log.warn("【延时任务】重试已达最大次数={},taskId={}", this.getMaxRetryTimes(), this.getTaskId());
            }
        }
    }

    @Override
    public String getTaskId() {
        return String.valueOf(orderId);
    }
}
