package com.coding.flyin.starter.http.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "flyin.http")
@Slf4j
public class HttpProperties {

    /** 整个连接池的最大连接数：默认 20个. */
    private int maxTotal = 20;

    /** 每个route默认的连接数：默认 2个. */
    private int defaultMaxPerRoute = 2;

    /** 空闲永久连接检查间隔：官方推荐使用这个来检查永久链接的可用性，而不推荐每次请求的时候才去检查（单位：毫秒）：默认不启用，推荐 2000. */
    private int validateAfterInactivity = 2000;

    /** 某个请求：跟目标服务建立连接超时时间，根据业务情况而定：默认永不超时，推荐3000. */
    private int connectTimeout = 3000;

    /** 某个请求：请求的超时时间(建立连接后，等待response返回的时间)：默认永不超时. */
    private int socketTimeout = 5000;

    /** 某个请求：从连接池中获取连接的超时时间：默认永不超时. */
    private int connectionRequestTimeout = 3000;

    /**
     * 建议所有的maxPerRoute的总和，小于系统支持线程总量的一半.
     *
     * <p>创建时间: <font style="color:#00FFFF">20180610 09:59</font><br>
     *
     * <p>示例如下： <br>
     * key=www.baidu.com, value=100<br>
     * key=http:www.baidu.com, value=100<br>
     * key=https:www.baidu.com, value=100<br>
     * key=http:www.baidu.com:80, value=100<br>
     * key=https:www.baidu.com:80, value=100<br>
     * 其中，key的默认schema是http，默认端口是80
     *
     * @since 0.1.0
     */
    private Map<String, Integer> maxPerRouteMap = new HashMap<>();

    /**
     * 设置访问主机的长连接请求默认保持时间，单位：秒.
     *
     * <p>创建时间: <font style="color:#00FFFF">20180609 13:55</font><br>
     * [请在此输入功能详述]
     *
     * @since 0.1.0
     */
    private Map<String, Integer> keepAliveHostMap = new HashMap<>();

    // ==================================================华丽的分割线==================================================

    public final String userAgent;

    /** 默认连接配置. */
    private final ConnectionConfig defaultConnectionConfig;

    /** 默认链接保持策略. */
    private final ConnectionKeepAliveStrategy defaultConnectionStrategy;

    /** 默认重试处理器. */
    private final DefaultHttpRequestRetryHandler defaultHttpRequestRetryHandler;

    public HttpProperties() {
        userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)";

        // 默认连接配置
        defaultConnectionConfig =
                ConnectionConfig.custom().setCharset(StandardCharsets.UTF_8).build();

        // 连接保持策略
        defaultConnectionStrategy =
                (response, context) -> {
                    HeaderElementIterator it =
                            new BasicHeaderElementIterator(
                                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                    HttpHost target =
                            (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
                    String hostName = target.getHostName().toLowerCase();
                    while (it.hasNext()) {
                        HeaderElement he = it.nextElement();
                        String param = he.getName();
                        String value = he.getValue();
                        if (value != null && param.equalsIgnoreCase("timeout")) {
                            try {
                                log.info(
                                        "【连接保持策略】hostName={},key={},value={}",
                                        hostName,
                                        param,
                                        value);
                                return Long.parseLong(value) * 1000;
                            } catch (final NumberFormatException ignore) {
                            }
                        }
                    }
                    // otherwise keep alive for 60 seconds
                    return keepAliveHostMap.getOrDefault(hostName, 60) * 1000;
                };

        defaultHttpRequestRetryHandler = new DefaultHttpRequestRetryHandler();
    }

    @Override
    public String toString() {
        return "HttpProperties{"
                + "maxTotal="
                + maxTotal
                + ", defaultMaxPerRoute="
                + defaultMaxPerRoute
                + ", validateAfterInactivity="
                + validateAfterInactivity
                + ", connectTimeout="
                + connectTimeout
                + ", socketTimeout="
                + socketTimeout
                + ", connectionRequestTimeout="
                + connectionRequestTimeout
                + ", maxPerRouteMap="
                + maxPerRouteMap
                + ", keepAliveHostMap="
                + keepAliveHostMap
                + '}';
    }
}
