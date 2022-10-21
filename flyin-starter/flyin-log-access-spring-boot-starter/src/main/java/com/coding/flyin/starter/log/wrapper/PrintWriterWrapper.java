package com.coding.flyin.starter.log.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class PrintWriterWrapper extends PrintWriter {
    private final ByteArrayOutputStream innerOut;
    private final HttpServletResponse response;

    public PrintWriterWrapper(ByteArrayOutputStream innerOut, HttpServletResponse response) {
        super(innerOut);
        this.innerOut = innerOut;
        this.response = response;
    }

    // 避免静态资源无法应答
    @Override
    public void write(int c) {
        super.write(c);
        try {
            response.getWriter().write(c);
        } catch (IOException e) {
            e.printStackTrace();
            this.setError();
        }
    }

    // 避免静态资源无法应答
    @Override
    public void write(String s, int off, int len) {
        super.write(s, off, len);
        try {
            response.getWriter().write(s, off, len);
        } catch (IOException e) {
            e.printStackTrace();
            this.setError();
        }
    }
}
