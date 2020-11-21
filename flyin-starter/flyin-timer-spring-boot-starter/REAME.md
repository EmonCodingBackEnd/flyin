# flyin-timer-spring-boot-starter

`flyin-timer-spring-boot-starter`用于帮助你在Spring Boot项目中轻松集成三种特殊任务：延时任务、调度任务以及异步任务。

## 如何使用

1. 在SpringBoot项目中加入`flyin-timer-spring-boot-starter`依赖

`Maven`

```xml
<dependency>
    <groupId>com.coding.flyin</groupId>
    <artifactId>flyin-timer-spring-boot-starter</artifactId>
    <version>0.1.2</version> <!-- 需要替换为最新版本 -->
</dependency>
```

2. 添加配置

```yaml
flyin:
  timer:
    enabled: false # 默认 false
    delay:
      enabled: true # 默认 false
      delay-task-queue-redis-key: flyin:delayQueue
      thread-name-prefix: TIMER_DELAY-
      core-pool-size: 8
      max-pool-size: 10
      queue-capacity: 100
      keey-alive-second: 60
      await-termination-seconds: 60
      delay-task-queue-daemon-thread-name: DelayTaskQueueDaemonThread
    schedule:
      enabled: true # 默认 false
      thread-name-prefix: TIMER_SCHEDULE-
      pool-size: 10
      await-termination-seconds: 60
    async:
      enabled: true # 默认 false
      thread-name-prefix: TIMER_ASYNE-
      core-pool-size: 8
      max-pool-size: 10
      queue-capacity: 100
      keey-alive-second: 60
      await-termination-seconds: 60
```

3. 引入使用

- 异步任务和调度任务，请参考`ExampleAsyncTask`和`ExampleScheduleTask`
- 延时任务：`ExampleDelayTask`

```java
ShareDelayedQueue.put(new ExampleDelayTask(1L), 10, TimeUnit.SECONDS);
```

通过`ShareDelayedQueue.put`方法加入任务，并指定延时多久调用。



