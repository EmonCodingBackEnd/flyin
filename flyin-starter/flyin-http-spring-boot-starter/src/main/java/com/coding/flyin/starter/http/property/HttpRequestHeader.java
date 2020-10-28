package com.coding.flyin.starter.http.property;

import org.springframework.http.HttpHeaders;

/**
 * Http请求头属性定义.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180608 14:17</font><br>
 * 为什么要创建这个类，因为无论Apache、SpringFramework还是Google提供的HttpHeaders都有包含不到的属性。
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpRequestHeader extends HttpHeaders {

    /** 非标准Http请求头 */
    public static final String KEEP_ALIVE = "Keep-Alive";
}
