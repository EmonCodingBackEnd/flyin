//package com.coding.flyin;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.commons.lang.StringUtils;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 注册应用信息至zookeeper
// */
//@Slf4j
//public class ZkDockerService {
//    private CuratorZookeeperClient zkClient;
//
//    private final Set<String> zkPathList = new HashSet<>();
//
//    // 失败重试定时器，定时检查是否有请求失败，如有，无限次重试
//    private ScheduledFuture<?> retryFuture;
//
//    // 定时任务执行器
//    private final ScheduledExecutorService retryExecutor =
//        Executors.newScheduledThreadPool(1, new NamedThreadFactory("RegistryFailedRetryTimer", true));
//
//    // 需要重新注册的数据
//    private Set<ClientData> retrySet = new HashSet<ClientData>();
//
//    /**
//     * init-method，初始化执行 将本机docker的IP地址 端口都注册到zookeeper中
//     */
//    public void register2Zookeeper() {
//        try {
//            zkClient = CuratorZookeeperClient.getInstance(ZOOKEEPER_ADDRESS);
//            ClientData client = findClientData();
//            registerClientData(client);
//            zkClient.addStateListener(new ZkStateListener() {
//                @Override
//                public void reconnected() {
//                    ClientData client = findClientData();
//                    // 将服务添加到重试列表
//                    retrySet.add(client);
//                }
//            });
//            // 启动线程进行重试，1秒执行一次，因为jobcenter的定时触发时间最短的是1秒
//            this.retryFuture = retryExecutor.scheduleWithFixedDelay(new Runnable() {
//                public void run() {
//                    // 检测并连接注册中心
//                    try {
//                        retryRegister();
//                    } catch (Throwable t) { // 防御性容错
//                        log.error("Unexpected error occur at failed retry, cause: " + t.getMessage(), t);
//                    }
//                }
//            }, 1, 1, TimeUnit.SECONDS);
//
//        } catch (Exception e) {
//            log.error("zookeeper write exception", e);
//        }
//    }
//
//    /**
//     * destrory-method,销毁时执行
//     */
//    public void destroy4Zookeeper() {
//        log.info("zkDockerService destrory4Zookeeper path=" + zkPathList);
//        try {
//            if (retryFuture != null) {
//                retryFuture.cancel(true);
//            }
//
//        } catch (Throwable t) {
//            log.warn(t.getMessage(), t);
//        }
//
//        if (zkPathList != null && zkPathList.size() > 0) {
//            for (String path : zkPathList) {
//                try {
//                    zkClient.delete(path);
//                } catch (Exception e) {
//                    log.error("zkDockerService destrory4Zookeeper exception", e);
//                }
//            }
//        }
//        zkClient.close();
//    }
//
//    /** 构造要存储的对象 **/
//    private ClientData findClientData() {
//        ClientData client = new ClientData();
//        client.setIpAddress(ip);
//        client.setPort(port);
//        client.setSource(1);
//        return client;
//    }
//
//    /** 将值写入zookeeper中 **/
//    private void registerClientData(ClientData client) throws Exception {
//        String centerPath = "/server";
//        String content = "";
//        String strServer = zkClient.write(centerPath, content);
//        if (!StringUtils.isBlank(strServer)) {
//            zkPathList.add(strServer);
//        }
//    }
//
//    /**
//     * 重连到zookeeper时，自动重试
//     */
//    protected synchronized void retryRegister() {
//        if (!retrySet.isEmpty()) {
//            log.info("jobclient  begin retry register client to zookeeper");
//            Set<ClientData> retryClients = new HashSet<ClientData>(retrySet);
//            for (ClientData data : retryClients) {
//                log.info("retry register=" + data);
//                try {
//                    registerJobcenterClient(data);
//                    retrySet.remove(data);
//                } catch (Exception e) {
//                    log.error("registerJobcenterClient failed", e);
//                }
//            }
//        }
//    }
//}
