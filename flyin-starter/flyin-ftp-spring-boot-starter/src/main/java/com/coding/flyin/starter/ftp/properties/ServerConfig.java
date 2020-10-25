package com.coding.flyin.starter.ftp.properties;

import com.coding.flyin.util.SensitiveUtils;
import lombok.Data;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;

@Data
@ConfigurationProperties(prefix = "flyin.ftp.server")
public class ServerConfig {

    private static final String DEFAULT_ACCESS_URL_PREFIX = "";

    /** ftp服务器地址. */
    private String host;

    /** 端口号：默认 21. */
    private int port = FTPClient.DEFAULT_PORT;

    /** 登录用户名. */
    private String username;

    /** 登录密码. */
    private String password;

    /** ftp服务器编码：默认 UTF_8. */
    private String encoding = StandardCharsets.UTF_8.name();

    /** 连接超时时间（单位：毫秒）. */
    private int connectTimeout = 5000;

    /** 数据传输超时时间（单位：毫秒）. */
    private int dataTimeout = 3000;

    /** 数据传输缓冲区大小：默认 1024bytes. */
    private int bufferSize = 1024;

    /** 传输文件类型：默认二进制. */
    private int transferFileType = FTPClient.BINARY_FILE_TYPE;

    /** ftp服务器是否被动模式. */
    private boolean passiveMode = Boolean.TRUE;

    // 访问上传的文件时，url前缀，比如 http://file.emon.vip/ 或者 http://192.168.1.116:80/
    private String accessUrlPrefixes = DEFAULT_ACCESS_URL_PREFIX;

    @Override
    public String toString() {
        return "ServerConfig{"
                + "host='"
                + host
                + '\''
                + ", port="
                + port
                + ", username='"
                + SensitiveUtils.desensitization(username)
                + '\''
                + ", password='"
                + SensitiveUtils.desensitization(password)
                + '\''
                + ", encoding='"
                + encoding
                + '\''
                + ", connectTimeout="
                + connectTimeout
                + ", dataTimeout="
                + dataTimeout
                + ", bufferSize="
                + bufferSize
                + ", transferFileType="
                + transferFileType
                + ", passiveMode="
                + passiveMode
                + ", accessUrlPrefixes='"
                + accessUrlPrefixes
                + '\''
                + '}';
    }
}
