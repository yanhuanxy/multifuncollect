package com.yanhuanxy.multifunexport.fileservice.operation.download;

import com.yanhuanxy.multifunexport.fileservice.dto.operation.DownloadFile;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yym
 * @version 1.0
 */
public abstract class Downloader {
    protected static final Logger logger = LoggerFactory.getLogger(Downloader.class);

    public void download(HttpServletResponse httpServletResponse, DownloadFile downloadFile) {

        InputStream inputStream = getInputStream(downloadFile);
        OutputStream outputStream = null;
        try {
            outputStream = httpServletResponse.getOutputStream();
            org.apache.commons.io.IOUtils.copyLarge(inputStream, outputStream);
        } catch (IOException e) {
            logger.error("文件流下载异常", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }

    }
    public abstract InputStream getInputStream(DownloadFile downloadFile);
}
