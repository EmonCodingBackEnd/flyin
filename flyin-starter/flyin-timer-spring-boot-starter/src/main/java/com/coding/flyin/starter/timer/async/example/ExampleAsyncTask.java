package com.coding.flyin.starter.timer.async.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 演示版异步任务，如有需要，请拷贝到具体微服务修改.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180613 14:31</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
// @Component
@Slf4j
public class ExampleAsyncTask {

    @Autowired private ObjectMapper objectMapper;

    // 获取异步结果：可等待获取
    //    @Async
    public Future<Long> asyncTask11(int startNumber, int endNumber) throws InterruptedException {
        log.info("asyncTask11开始执行");
        long beg = System.currentTimeMillis();
        long sum = 0;
        for (int i = startNumber; i <= endNumber; i++) {
            sum += i;
        }
        TimeUnit.SECONDS.sleep(2);
        long end = System.currentTimeMillis();
        log.info("asyncTask11执行耗时={}ms", end - beg);
        return new AsyncResult<>(sum);
    }

    // 获取异步结果
    //    @Async
    public void asyncTask22(int startNumber, int endNumber) throws InterruptedException {
        log.info("asyncTask22开始执行");
        long beg = System.currentTimeMillis();
        TimeUnit.SECONDS.sleep(2);
        long end = System.currentTimeMillis();
        log.info("asyncTask22执行耗时={}ms", end - beg);
    }
}
