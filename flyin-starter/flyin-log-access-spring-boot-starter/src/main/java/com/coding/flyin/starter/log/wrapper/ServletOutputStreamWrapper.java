package com.coding.flyin.starter.log.wrapper;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

public class ServletOutputStreamWrapper extends ServletOutputStream {
    private final OutputStream innerOut;
    private final HttpServletResponse response;

    public ServletOutputStreamWrapper(OutputStream innerOut, HttpServletResponse response) {
        super();
        this.innerOut = innerOut;
        this.response = response;
    }

    @Override
    public boolean isReady() {
        if (response == null) {
            return false;
        }
        try {
            return response.getOutputStream().isReady();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        if (response != null) {
            try {
                response.getOutputStream().setWriteListener(writeListener);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 避免静态资源无法应答
    @Override
    public void write(int b) throws IOException {
        if (response != null) {
            response.getOutputStream().write(b);
        }
        innerOut.write(b);
    }
}
