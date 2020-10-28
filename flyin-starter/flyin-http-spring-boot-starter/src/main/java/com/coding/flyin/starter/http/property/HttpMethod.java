package com.coding.flyin.starter.http.property;

import org.apache.http.client.methods.*;

/**
 * Http支持的方法定义.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180607 14:27</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
public enum HttpMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    public static HttpRequestBase getHttpRequest(String url, HttpMethod httpMethod) {
        HttpRequestBase httpRequest;

        switch (httpMethod) {
            case GET:
                httpRequest = new HttpGet(url);
                break;
            case HEAD:
                httpRequest = new HttpHead(url);
                break;
            case POST: // 实现了 HttpEntityEnclosingRequest 接口
                httpRequest = new HttpPost(url);
                break;
            case PUT: // 实现了 HttpEntityEnclosingRequest 接口
                httpRequest = new HttpPut(url);
                break;
            case PATCH: // 实现了 HttpEntityEnclosingRequest 接口
                httpRequest = new HttpPatch(url);
                break;
            case DELETE:
                httpRequest = new HttpDelete(url);
                break;
            case OPTIONS:
                httpRequest = new HttpOptions(url);
                break;
            case TRACE:
                httpRequest = new HttpTrace(url);
                break;
            default:
                httpRequest = new HttpPost(url);
        }
        return httpRequest;
    }
}
