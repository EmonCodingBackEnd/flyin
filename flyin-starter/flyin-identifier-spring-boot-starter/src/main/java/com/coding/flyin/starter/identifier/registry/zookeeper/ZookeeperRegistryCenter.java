package com.coding.flyin.starter.identifier.registry.zookeeper;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import com.coding.flyin.starter.identifier.exception.RegistryExceptionHandler;
import com.coding.flyin.starter.identifier.properties.ZookeeperProperties;
import com.coding.flyin.starter.identifier.registry.RegistryCenter;
import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

/**
 * Zookeeper注册中心
 */
@Slf4j
public class ZookeeperRegistryCenter implements RegistryCenter {

    private final Map<String, TreeCache> caches = new HashMap<>();

    private final ZookeeperProperties zookeeperProperties;

    private CuratorFramework client;

    public ZookeeperRegistryCenter(ZookeeperProperties zookeeperProperties) {
        this.zookeeperProperties = zookeeperProperties;
    }

    @Override
    public synchronized void init() {
        if (client != null) {
            // client已经初始化，直接重置返回
            return;
        }
        log.debug("init zookeeper registry, connect to servers : {}", zookeeperProperties.getConnectString());
        CuratorFrameworkFactory.Builder builder =
            CuratorFrameworkFactory.builder().connectString(zookeeperProperties.getConnectString())
                .retryPolicy(new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(),
                    zookeeperProperties.getMaxRetries(), zookeeperProperties.getMaxSleepMs()))
                .namespace(zookeeperProperties.getNamespace());
        if (0 != zookeeperProperties.getSessionTimeoutMs()) {
            builder.sessionTimeoutMs(zookeeperProperties.getSessionTimeoutMs());
        }
        if (0 != zookeeperProperties.getConnectionTimeoutMs()) {
            builder.connectionTimeoutMs(zookeeperProperties.getConnectionTimeoutMs());
        }
        if (zookeeperProperties.getDigest() != null && !zookeeperProperties.getDigest().isEmpty()) {
            builder.authorization("digest", zookeeperProperties.getDigest().getBytes(StandardCharsets.UTF_8))
                .aclProvider(new ACLProvider() {

                    @Override
                    public List<ACL> getDefaultAcl() {
                        return ZooDefs.Ids.CREATOR_ALL_ACL;
                    }

                    @Override
                    public List<ACL> getAclForPath(final String path) {
                        return ZooDefs.Ids.CREATOR_ALL_ACL;
                    }
                });
        }
        client = builder.build();
        client.start();
        try {
            if (!client.blockUntilConnected(zookeeperProperties.getMaxSleepMs() * zookeeperProperties.getMaxRetries(),
                TimeUnit.MILLISECONDS)) {
                client.close();
                throw new KeeperException.OperationTimeoutException();
            }
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
        }
    }

    @Override
    public void close() {
        if (client == null) {
            return;
        }
        for (Entry<String, TreeCache> each : caches.entrySet()) {
            each.getValue().close();
        }
        waitForCacheClose();
        CloseableUtils.closeQuietly(client);
        // 重置client状态
        client = null;
    }

    /**
     * TODO 等待500ms, cache先关闭再关闭client, 否则会抛异常 因为异步处理, 可能会导致client先关闭而cache还未关闭结束. 等待Curator新版本解决这个bug.
     * BUG地址：https://issues.apache.org/jira/browse/CURATOR-157
     */
    private void waitForCacheClose() {
        try {
            Thread.sleep(500L);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String get(final String key) {
        TreeCache cache = findTreeCache(key);
        if (null == cache) {
            return getDirectly(key);
        }
        ChildData resultInCache = cache.getCurrentData(key);
        if (null != resultInCache) {
            return null == resultInCache.getData() ? null : new String(resultInCache.getData(), StandardCharsets.UTF_8);
        }
        return getDirectly(key);
    }

    private TreeCache findTreeCache(final String key) {
        for (Entry<String, TreeCache> entry : caches.entrySet()) {
            if (key.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public String getDirectly(final String key) {
        try {
            return new String(client.getData().forPath(key), StandardCharsets.UTF_8);
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
            return null;
        }
    }

    @Override
    public List<String> getChildrenKeys(final String key) {
        try {
            List<String> result = client.getChildren().forPath(key);
            result.sort(Comparator.reverseOrder());
            return result;
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
            return Collections.emptyList();
        }
    }

    @Override
    public int getNumChildren(final String key) {
        try {
            Stat stat = client.checkExists().forPath(key);
            if (null != stat) {
                return stat.getNumChildren();
            }
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
        }
        return 0;
    }

    @Override
    public boolean isExisted(final String key) {
        try {
            return null != client.checkExists().forPath(key);
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
            return false;
        }
    }

    @Override
    public void persist(final String key, final String value) {
        try {
            if (!isExisted(key)) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key,
                    value.getBytes(StandardCharsets.UTF_8));
            } else {
                update(key, value);
            }
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
        }
    }

    @Override
    public void update(final String key, final String value) {
        try {
            // client.transactionOp().setData().forPath(key, value.getBytes(StandardCharsets.UTF_8));
            client.inTransaction().check().forPath(key).and().setData()
                .forPath(key, value.getBytes(StandardCharsets.UTF_8)).and().commit();
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
        }
    }

    @Override
    public void persistEphemeral(final String key, final String value) {
        try {
            if (isExisted(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key,
                value.getBytes(StandardCharsets.UTF_8));
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
        }
    }

    @Override
    public String persistSequential(final String key, final String value) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(key,
                value.getBytes(StandardCharsets.UTF_8));
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
        }
        return null;
    }

    @Override
    public void persistEphemeralSequential(final String key) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
        }
    }

    @Override
    public void remove(final String key) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(key);
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
        }
    }

    @Override
    public long getRegistryCenterTime(final String key) {
        long result = 0L;
        try {
            persist(key, "");
            result = client.checkExists().forPath(key).getMtime();
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
        }
        Preconditions.checkState(0L != result, "Cannot get registry center time.");
        return result;
    }

    @Override
    public Object getRawClient() {
        if (client == null) {
            init();
        }
        return client;
    }

    @Override
    public void addCacheData(final String cachePath) {
        TreeCache cache = new TreeCache(client, cachePath);
        try {
            cache.start();
        } catch (final Exception ex) {
            RegistryExceptionHandler.handleException(ex);
        }
        caches.put(cachePath + "/", cache);
    }

    @Override
    public void evictCacheData(final String cachePath) {
        TreeCache cache = caches.remove(cachePath + "/");
        if (null != cache) {
            cache.close();
        }
    }

    @Override
    public Object getRawCache(final String cachePath) {
        return caches.get(cachePath + "/");
    }
}
