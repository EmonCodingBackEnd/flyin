package com.coding.flyin;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.coding.flyin.zookeepertest.CuratorZookeeperClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZookeeperHigherTests {
    public static final String connectString = "repo.emon.vip:2181";

    private CuratorZookeeperClient zkClient;

    @Before
    public void before() {
        zkClient = CuratorZookeeperClient.getInstance(connectString);
    }

    @After
    public void after() {
        zkClient.close();
    }

    @Test
    public void testCreate() throws Exception {
        // /a/b/c0000000001
        String path = zkClient.write("/a/b/c", "liming");
        Assert.assertTrue(path.startsWith("/a/b/c"));
    }

    @Test
    public void testReadRandom() throws Exception {
        for (int i = 0; i < 10; i++) {
            String pathValue = zkClient.readRandom("/test");
            System.out.println(pathValue);
        }
    }

    @Test
    public void testReadAll() throws Exception {
        List<String> pathList = zkClient.readAll("/test/");
        if (pathList != null) {
            pathList.forEach(System.out::println);
        }
    }

    @Test
    public void testDelete() throws Exception {
        zkClient.delete("/test");
    }

    @Test
    public void testGetChildren() throws Exception {
        List<String> children = zkClient.getChildren("/test");
        if (children != null) {
            children.forEach(System.out::println);
        }
    }

}
