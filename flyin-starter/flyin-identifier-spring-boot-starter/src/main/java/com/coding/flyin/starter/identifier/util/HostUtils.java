package com.coding.flyin.starter.identifier.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Host Utils
 */
public class HostUtils {

    /**
     * 获取本地IP
     * 
     * @return 本地IP
     * @throws UnknownHostException UnknownHostException
     */
    public static String getLocalIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * 获取本地主机名
     * 
     * @return 本地主机名
     * @throws UnknownHostException UnknownHostException
     */
    public static String getLocalHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }
}
