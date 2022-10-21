package com.coding.flyin.starter.log.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class RepeatedlyResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream byteArrayOutputStream;
    private final ServletOutputStream outputStream;
    private final PrintWriter printWriter;

    public RepeatedlyResponseWrapper(HttpServletResponse response) {
        super(response);
        this.byteArrayOutputStream = new ByteArrayOutputStream(2048);
        // 这里将response也传入了
        this.outputStream = new ServletOutputStreamWrapper(byteArrayOutputStream, response);
        this.printWriter = new PrintWriterWrapper(byteArrayOutputStream, response);
    }

    public byte[] getResponseData() throws IOException {
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return printWriter;
    }
}
