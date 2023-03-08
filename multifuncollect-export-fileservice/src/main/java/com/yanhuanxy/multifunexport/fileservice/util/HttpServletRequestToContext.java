package com.yanhuanxy.multifunexport.fileservice.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.RequestContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * 转换 HttpServletRequest
 */
public class HttpServletRequestToContext implements RequestContext {
    HttpServletRequest request = null;

    public HttpServletRequestToContext(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getCharacterEncoding() {
        return request.getCharacterEncoding();
    }

    @Override
    public String getContentType() {
        return request.getContentType();
    }

    @Override
    public int getContentLength() {
        return request.getContentLength();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return request.getInputStream();
    }
}
