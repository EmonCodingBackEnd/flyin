package com.coding.flyin.zookeepertest;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CuratorZookeeperClient {

    private CuratorFramework curator;

    private volatile static CuratorZookeeperClient instance;

    /**
     * ==>key:父路径，如/jobcenter/client/goodscenter
     * 
     * ==>value：Map<key,value>
     * 
     * ====>key:子路径，如/jobcenter/client/goodscenter/goodscenter00000001
     * 
     * ====>value:路径中的值
     */
    private static final ConcurrentHashMap<String, Map<String, String>> zkCacheMap = new ConcurrentHashMap<>();

    public static Map<String, Map<String, String>> getZkCacheMap() {
        return zkCacheMap;
    }

    public static CuratorZookeeperClient getInstance(String zkServers) {
        if (instance == null) {
            synchronized (CuratorZookeeperClient.class) {
                if (instance == null) {
                    log.info("initial CuratorZookeeperClient instance");
                    instance = new CuratorZookeeperClient(zkServers);
                }
            }
        }
        return instance;
    }

    private CuratorZookeeperClient(String zkServers) {
        curator = newCurator(zkServers);
        curator.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            public void stateChanged(CuratorFramework client, ConnectionState state) {
                if (state == ConnectionState.LOST) {
                    // 连接丢失
                    log.info("lost session with zookeeper");
                } else if (state == ConnectionState.CONNECTED) {
                    // 连接新建
                    log.info("connected with zookeeper");
                } else if (state == ConnectionState.RECONNECTED) {
                    // 连接重连
                    log.info("reconnected with zookeeper");
                    for (ZkStateListener listener : stateListeners) {
                        listener.reconnected();
                    }
                }
            }
        });
        curator.start();
    }

    private CuratorFramework newCurator(String zkServers) {
        return CuratorFrameworkFactory.builder().connectString(zkServers)
            .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
            // 该条会话在 ZooKeeper 服务端的失效时间
            .sessionTimeoutMs(60 * 1000)
            // 用来限制客户端发起一个会话连接到接收 ZooKeeper 服务端应答的时间
            .connectionTimeoutMs(60 * 1000).build();
    }

    /**
     * 写数据：/docker/jobcenter/client/app/app0..../app1...../app2
     * 
     * @param path -
     * @param content -
     *
     * @return 返回真正写到的路径
     * @throws Exception -
     */
    public String write(String path, String content) throws Exception {
        return curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path,
            content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 随机读取一个path的直接子路径。
     * 
     * 先从cache中读取，如果没有，再从zookeeper中查询
     * 
     * @param path -
     * @return
     * @throws Exception
     */
    public String readRandom(String path) throws Exception {
        Assert.notNull(path, "parameter path must be not null");
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        String parentPath = path;
        Map<String, String> cacheMap = zkCacheMap.get(path);
        if (!CollectionUtils.isEmpty(cacheMap)) {
            log.debug("get random value from cache,path=" + path);
            return getRandomValueFromMap(cacheMap);
        }
        if (curator.checkExists().forPath(path) == null) {
            log.debug("path [{}] is not exists,return null", path);
            return null;
        } else {
            log.debug("read random from zookeeper,path=" + path);
            cacheMap = new HashMap<>();
            List<String> list = curator.getChildren().usingWatcher(new ZKWatcher(parentPath, path)).forPath(path);
            if (CollectionUtils.isEmpty(list)) {
                log.debug("path [{}] has no children return null", path);
                return null;
            }

            Random rand = new Random();
            String child = list.get(rand.nextInt(list.size()));
            path = path + "/" + child;
            byte[] b = curator.getData().usingWatcher(new ZKWatcher(parentPath, path)).forPath(path);
            String value = null;
            if (b != null && b.length > 0) {
                value = new String(b, StandardCharsets.UTF_8);
                if (StringUtils.isNotBlank(value)) {
                    cacheMap.put(path, value);
                    zkCacheMap.put(parentPath, cacheMap);
                }
            }
            return value;
        }
    }

    /**
     * 随机获取Map中的一个值
     *
     * @param map - 指定map
     * @return - 随机获取的值
     */
    private String getRandomValueFromMap(Map<String, String> map) {
        Object[] values = map.values().toArray();
        Random rand = new Random();
        return values[rand.nextInt(values.length)].toString();
    }

    /**
     * 读取指定path下的直接子路径内容。
     * 
     * 会先从本地缓存中读取，如果读取不到，再去zookeeper中读取。
     * 
     * @param path -
     * @return -
     * @throws Exception -
     */
    public List<String> readAll(String path) throws Exception {
        Assert.notNull(path, "parameter path must be not null");
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        String parentPath = path;
        Map<String, String> cacheMap = zkCacheMap.get(path);
        List<String> list = new ArrayList<>();
        if (cacheMap != null) {
            log.debug("read all from cache,path=" + path);
            list.addAll(cacheMap.values());
            return list;
        }
        if (curator.checkExists().forPath(path) == null) {
            log.debug("path [{}] is not exists,return null", path);
            return null;
        } else {
            cacheMap = new HashMap<>();
            List<String> children = curator.getChildren().usingWatcher(new ZKWatcher(parentPath, path)).forPath(path);
            if (CollectionUtils.isEmpty(children)) {
                log.debug("path [{}] has no children,return null", path);
                return null;
            } else {
                log.debug("read all from zookeeper,path=" + path);
                String basePath = path;
                for (String child : children) {
                    path = basePath + (basePath.endsWith("/") ? "" : "/") + child;
                    byte[] b = curator.getData().usingWatcher(new ZKWatcher(parentPath, path)).forPath(path);
                    if (b != null && b.length > 0) {
                        String value = new String(b, StandardCharsets.UTF_8);
                        if (StringUtils.isNotBlank(value)) {
                            list.add(value);
                            cacheMap.put(path, value);
                        }
                    }
                }
            }
            zkCacheMap.put(parentPath, cacheMap);
            return list;
        }
    }

    /**
     * 级联删除指定路径。
     * 
     * @param path -
     * @throws Exception -
     */
    public void delete(String path) throws Exception {
        Assert.notNull(path, "parameter path must be not null");
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        if (curator.checkExists().forPath(path) != null) {
            curator.delete().inBackground().forPath(path);
            zkCacheMap.remove(path);
        }
    }

    /**
     * 获取路径下的所有直接子路径（非递归）
     * 
     * @param path -
     * @return -
     */
    public List<String> getChildren(String path) throws Exception {
        Assert.notNull(path, "parameter path must be not null");
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        if (curator.checkExists().forPath(path) == null) {
            log.debug("path [{}] is not exists,return null", path);
            return null;
        } else {
            return curator.getChildren().forPath(path);
        }
    }

    public void close() {
        if (curator != null) {
            curator.close();
            curator = null;
        }
        zkCacheMap.clear();
    }

    /**
     * zookeeper监听节点数据变化
     */
    private class ZKWatcher implements CuratorWatcher {
        private final String parentPath;
        private final String path;

        public ZKWatcher(String parentPath, String path) {
            this.parentPath = parentPath;
            this.path = path;
        }

        public void process(WatchedEvent event) throws Exception {
            Map<String, String> cacheMap = zkCacheMap.get(parentPath);
            if (cacheMap == null) {
                cacheMap = new HashMap<>();
            }
            if (event.getType() == Event.EventType.NodeDataChanged || event.getType() == Event.EventType.NodeCreated) {
                byte[] data = curator.getData().usingWatcher(this).forPath(path);
                cacheMap.put(path, new String(data, StandardCharsets.UTF_8));
                log.info("add cache={}", new String(data, StandardCharsets.UTF_8));
            } else if (event.getType() == Event.EventType.NodeDeleted) {
                cacheMap.remove(path);
                log.info("remove cache path={}", path);
            } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
                // 子节点发生变化，重新进行缓存
                cacheMap.clear();
                List<String> children =
                    curator.getChildren().usingWatcher(new ZKWatcher(parentPath, path)).forPath(path);
                if (children != null && children.size() > 0) {
                    for (String child : children) {
                        String childPath = parentPath + "/" + child;
                        byte[] b =
                            curator.getData().usingWatcher(new ZKWatcher(parentPath, childPath)).forPath(childPath);
                        String value = new String(b, StandardCharsets.UTF_8);
                        if (StringUtils.isNotBlank(value)) {
                            cacheMap.put(childPath, value);
                        }
                    }
                }
                log.info("node children changed,recaching path={}", path);
            }

            zkCacheMap.put(parentPath, cacheMap);
        }
    }

    private final Set<ZkStateListener> stateListeners = new CopyOnWriteArraySet<>();

    public void addStateListener(ZkStateListener listener) {
        stateListeners.add(listener);
    }
}
