package com.coding.flyin;

import java.nio.charset.StandardCharsets;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZookeeperBasicTests {
    public static final String connectString = "repo.emon.vip:2181";
    private static CuratorFramework curator;

    @Before
    public void before() {
        // 当客户端异常退出或者与服务端失去连接时，可以通过设置客户端重新连接ZooKeeper服务端。重试一组次数，重试之间的睡眠时间增加
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        curator = CuratorFrameworkFactory.builder().connectString(connectString)
            // 该条会话在 ZooKeeper 服务端的失效时间
            .sessionTimeoutMs(60 * 1000)
            // 用来限制客户端发起一个会话连接到接收 ZooKeeper 服务端应答的时间
            .connectionTimeoutMs(60 * 1000).retryPolicy(retryPolicy).namespace("curator/basic") // 包含隔离名称
            .build();
        curator.start();
    }

    @After
    public void after() {
        curator.close();
    }

    @Test
    public void testCreate() throws Exception {
        curator.create().forPath("/zkBasic", "zkBasic".getBytes(StandardCharsets.UTF_8));
    }

}
