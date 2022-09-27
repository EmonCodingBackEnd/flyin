package com.coding.flyin.zookeepertest;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * 线程前缀
     */
    private final String prefix;

    /**
     * 是否守护线程
     */
    private final boolean daemon;

    public NamedThreadFactory() {
        this("zkFlyin" + POOL_SEQ.getAndIncrement());
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        this.prefix = prefix + "-thread-";
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String threadName = prefix + threadNumber.getAndIncrement();
        Thread ret = new Thread(runnable, threadName);
        ret.setDaemon(daemon);
        return ret;
    }

}
