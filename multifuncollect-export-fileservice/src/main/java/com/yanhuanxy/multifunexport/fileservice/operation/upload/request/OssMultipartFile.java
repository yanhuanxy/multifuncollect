package com.yanhuanxy.multifunexport.fileservice.operation.upload.request;

import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public class OssMultipartFile {

    MultipartFile multipartFile = null;

    private OssMultipartFile() {
        super();
    }

    public OssMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getFileName() {
        String originalName = getMultipartFile().getOriginalFilename();
        if (originalName != null && !originalName.contains(".")) {
            return originalName;
        }
        return originalName.substring(0, originalName.lastIndexOf("."));
    }

    public String getExtendName() {
        String originalName = getMultipartFile().getOriginalFilename();
        return FilenameUtils.getExtension(originalName);
    }

    public String getFileUrl() {
        String uuid = UUID.randomUUID().toString();
        return OssUtils.getUploadFileUrl(null, uuid, getExtendName());
    }

    public String getFileUrl(String userCode, String identify) {
        return OssUtils.getUploadFileUrl(userCode, identify, getExtendName());
    }

    public InputStream getUploadInputStream() throws IOException {
        return getMultipartFile().getInputStream();
    }

    public byte[] getUploadBytes() throws IOException {
        return getMultipartFile().getBytes();
    }

    public long getSize() {
        return getMultipartFile().getSize();
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

}
