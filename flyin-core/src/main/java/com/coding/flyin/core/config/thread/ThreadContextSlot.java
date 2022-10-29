package com.coding.flyin.core.config.thread;

import org.springframework.core.Ordered;

/**
 * 以 java.util.concurrent.Executor 为模式说明如下：<br/>
 * java.util.concurrent.Executor#void execute(Runnable command);<br/>
 * 一般的场景下，Executor.execute(Runnable command)就完成了run()操作。<br/>
 *
 * 如果采用装饰者模式，把真正的run()方法包裹起来，会出现如下模式：
 * <hr><blockquote><pre>
 * beforeExecute();
 * Executor.execute(
 *  Exception exp = null;
 *  try {
 *      beforeRun();
 *      RunnableDecorator.run();
 *  } catch(Exception e) {
 *      exp = e;
 *  } finally {
 *    afterRun(exp);
 *  }
 * )
 * afterExecute();
 * </pre></blockquote><hr>
 */
public interface ThreadContextSlot extends Ordered {

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    };

    /**
     * 是否对某一类或某一个 Runnable 对象支持
     */
    boolean supportRunnable(Class<? extends Runnable> clazz);

    default void beforeExecute() {}

    default void beforeRun() {}

    default void afterRun(Exception e) {}

    default void afterExecute(Exception e) {}
}
