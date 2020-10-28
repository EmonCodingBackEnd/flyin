package com.coding.flyin.starter.http.generator;

import com.coding.flyin.starter.http.exception.HttpException;
import com.coding.flyin.starter.http.properties.HttpProperties;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Map;

/** Http同步客户端生成器 */
public class HttpSyncClientGenerator extends HttpClientBuilder {

    private final HttpProperties httpProperties;

    private int maxTotal;

    private int maxPerRoute;

    private Map<String, Integer> maxPerRouteMap;

    public HttpSyncClientGenerator(HttpProperties httpProperties) {
        this.httpProperties = httpProperties;
        this.maxPerRoute = httpProperties.getDefaultMaxPerRoute();
        this.maxTotal = httpProperties.getMaxTotal();
        this.maxPerRouteMap = httpProperties.getMaxPerRouteMap();

        this.setUserAgent(httpProperties.userAgent)
                .setKeepAliveStrategy(httpProperties.getDefaultConnectionStrategy());
    }

    /** 默认的ssl生成器，可以通过带参ssl方法重新指定证书. */
    private SSLGenerator defaultSSL = SSLGenerator.custom();

    /**
     * 设置默认证书
     *
     * @return -
     */
    public HttpSyncClientGenerator ssl() throws HttpException {
        this.defaultSSL = SSLGenerator.custom().ssl();
        return this;
    }

    /**
     * 设置自定义的sslContext
     *
     * @param keyStorePath - 密钥库路径
     * @return -
     */
    public HttpSyncClientGenerator ssl(String keyStorePath) {
        return ssl(keyStorePath, "nopassword");
    }

    /**
     * 设置自定义证书
     *
     * @param keyStorePath - 密钥库路径
     * @param keyStorePass - 密钥库密码
     * @return -
     */
    public HttpSyncClientGenerator ssl(String keyStorePath, String keyStorePass) {
        this.defaultSSL = SSLGenerator.custom().ssl(keyStorePath, keyStorePass);
        return this;
    }

    /**
     * 设置自定义证书
     *
     * @param keyStoreType - 密钥库类型
     * @param keyStorePath - 密钥库路径
     * @param keyStorePass - 密钥库密码
     * @return -
     */
    public HttpSyncClientGenerator ssl(
            String keyStoreType, String keyStorePath, String keyStorePass) {
        this.defaultSSL = SSLGenerator.custom().ssl(keyStoreType, keyStorePath, keyStorePass);
        return this;
    }

    /**
     * 设置默认连接池.
     *
     * @return -
     */
    public HttpSyncClientGenerator pool() {
        return pool(maxTotal, maxPerRoute);
    }

    /**
     * 设置连接池
     *
     * @param maxTotal - 最大连接数
     * @param maxPerRoute - 每个路由默认连接数
     * @return -
     */
    public HttpSyncClientGenerator pool(int maxTotal, int maxPerRoute) {
        return pool(maxTotal, maxPerRoute, null);
    }

    /**
     * 设置连接池
     *
     * @param maxTotal - 最大连接数
     * @param maxPerRoute - 路由默认的最大连接数
     * @param maxPerRouteMap - 针对具体路由的最大连接数，参见{@linkplain HttpProperties#maxPerRouteMap}
     * @return -
     */
    public HttpSyncClientGenerator pool(
            int maxTotal, int maxPerRoute, Map<String, Integer> maxPerRouteMap) {
        if (maxTotal > 0) {
            this.maxTotal = maxTotal;
        }
        if (maxPerRoute > 0) {
            this.maxPerRoute = maxPerRoute;
        }
        if (maxPerRouteMap != null) {
            this.maxPerRouteMap = maxPerRouteMap;
        }
        return this;
    }

    /**
     * 设置默认超时时间.
     *
     * @return -
     */
    public HttpSyncClientGenerator timeout() {
        return timeout(
                httpProperties.getConnectTimeout(),
                httpProperties.getSocketTimeout(),
                httpProperties.getConnectionRequestTimeout());
    }

    /**
     * 设置超时时间，单位：毫秒.
     *
     * @param connectionTimeout - 跟目标服务建立连接超时时间，根据业务情况而定
     * @param socketTimeout - 请求的超时时间(建立连接后，等待response返回的时间)
     * @param connectionRequestTimeout - 从连接池中获取连接的超时时间
     * @return -
     */
    public HttpSyncClientGenerator timeout(
            int connectionTimeout, int socketTimeout, int connectionRequestTimeout) {
        RequestConfig requestConfig =
                RequestConfig.custom()
                        .setConnectTimeout(connectionTimeout)
                        .setSocketTimeout(socketTimeout)
                        .setConnectionRequestTimeout(connectionRequestTimeout)
                        .build();
        return (HttpSyncClientGenerator) this.setDefaultRequestConfig(requestConfig);
    }

    /**
     * 设置代理.
     *
     * @param hostOrIP - 代理主机名或者IP地址
     * @param port - 代理端口
     * @return -
     */
    public HttpSyncClientGenerator proxy(String hostOrIP, int port) {
        HttpHost proxy = new HttpHost(hostOrIP, port, HttpHost.DEFAULT_SCHEME_NAME);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        return (HttpSyncClientGenerator) this.setRoutePlanner(routePlanner);
    }

    /**
     * 设置重试机制
     *
     * @return -
     */
    public HttpSyncClientGenerator retry() {
        return retry(httpProperties.getDefaultHttpRequestRetryHandler());
    }

    public HttpSyncClientGenerator retry(HttpRequestRetryHandler httpRequestRetryHandler) {
        this.setRetryHandler(httpRequestRetryHandler);
        return this;
    }

    @Override
    public CloseableHttpClient build() {
        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.INSTANCE)
                        .register("https", defaultSSL.getSslConnFactory())
                        .build();
        PoolingHttpClientConnectionManager poolingConnMgr =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingConnMgr.setMaxTotal(this.maxTotal);
        poolingConnMgr.setDefaultMaxPerRoute(this.maxPerRoute);
        for (Map.Entry<String, Integer> entry : this.maxPerRouteMap.entrySet()) {
            poolingConnMgr.setMaxPerRoute(
                    new HttpRoute(HttpHost.create(entry.getKey())), entry.getValue());
        }
        poolingConnMgr.setValidateAfterInactivity(httpProperties.getValidateAfterInactivity());
        poolingConnMgr.setDefaultConnectionConfig(httpProperties.getDefaultConnectionConfig());
        this.setConnectionManager(poolingConnMgr);
        return super.build();
    }
}
