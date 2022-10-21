package com.coding.flyin.starter.log.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StreamUtils;

public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] requestBody;

    public RepeatedlyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 如果先调用request.getInputStream()，会导致：controller读取不到x-www-form-urlencoded格式的数据的bug，这里先调用request.getParameterMap()
        // 参见 org.apache.catalina.connector.Request#parseParameters 原理
        request.getParameterMap();
        requestBody = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 这里每次都重新创建了一个InputStream
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
