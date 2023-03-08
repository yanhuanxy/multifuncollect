package com.yanhuanxy.multifunexport.fileservice.operation.download.product;

import com.yanhuanxy.multifunexport.fileservice.dto.operation.DownloadFile;
import com.yanhuanxy.multifunexport.fileservice.operation.download.Downloader;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class LocalStorageDownloader extends Downloader {

    @Override
    public InputStream getInputStream(DownloadFile downloadFile) {
        //设置文件路径
        File file = new File(OssUtils.getStaticPath() + downloadFile.getFileUrl());

        InputStream inputStream = null;
        try {
            if (downloadFile.getRange() != null) {
                RandomAccessFile randowAccessFile = new RandomAccessFile(file, "r");
                randowAccessFile.seek(downloadFile.getRange().getStart());
                byte[] bytes = new byte[downloadFile.getRange().getLength()];
                randowAccessFile.read(bytes);
                inputStream = new ByteArrayInputStream(bytes);
            } else {
                inputStream = new FileInputStream(file);
            }
        } catch (IOException e) {
            logger.error("服务器文件下载异常",e);
        }
        return inputStream;

    }
}
