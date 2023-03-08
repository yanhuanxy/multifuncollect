package com.yanhuanxy.multifunexport.fileservice.operation.upload.product;

import com.yanhuanxy.multifunexport.fileservice.dto.enums.StorageTypeEnums;
import com.yanhuanxy.multifunexport.fileservice.dto.enums.UploadFileStatusEnums;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.UploadFile;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.UploadFileResult;
import com.yanhuanxy.multifunexport.fileservice.exception.operation.UploadException;
import com.yanhuanxy.multifunexport.fileservice.operation.upload.Uploader;
import com.yanhuanxy.multifunexport.fileservice.operation.upload.request.OssMultipartFile;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

@Component
public class LocalStorageUploader extends Uploader {

    public static Map<String, String> FILE_URL_MAP = new HashMap<>();

    protected UploadFileResult doUploadFlow(OssMultipartFile ossMultipartFile, UploadFile uploadFile) {
        UploadFileResult uploadFileResult = new UploadFileResult();
        try {
            String fileUrl = OssUtils.getUploadFileUrl(uploadFile.getUserCode(), uploadFile.getIdentifier(), ossMultipartFile.getExtendName());
            if (StringUtils.isNotEmpty(FILE_URL_MAP.get(uploadFile.getIdentifier()))) {
                fileUrl = FILE_URL_MAP.get(uploadFile.getIdentifier());
            } else {
                FILE_URL_MAP.put(uploadFile.getIdentifier(), fileUrl);
            }
            String tempFileUrl = fileUrl + "_tmp";
            String confFileUrl = fileUrl.replace("." + ossMultipartFile.getExtendName(), ".conf");

            File file = new File(OssUtils.getStaticPath() + fileUrl);
            File tempFile = new File(OssUtils.getStaticPath() + tempFileUrl);
            File confFile = new File(OssUtils.getStaticPath() + confFileUrl);

            //第一步 打开将要写入的文件
            RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
            //第二步 打开通道
            try {
                FileChannel fileChannel = raf.getChannel();
                //第三步 计算偏移量
                long position = (uploadFile.getChunkNumber() - 1) * uploadFile.getChunkSize();
                //第四步 获取分片数据
                byte[] fileData = ossMultipartFile.getUploadBytes();
                //第五步 写入数据
                fileChannel.position(position);
                fileChannel.write(ByteBuffer.wrap(fileData));
                fileChannel.force(true);
                fileChannel.close();
            } finally {
                IOUtils.closeQuietly(raf);
            }

            //判断是否完成文件的传输并进行校验与重命名
            boolean isComplete = checkUploadStatus(uploadFile, confFile);
            uploadFileResult.setFileUrl(fileUrl);
            uploadFileResult.setFileName(ossMultipartFile.getFileName());
            uploadFileResult.setExtendName(ossMultipartFile.getExtendName());
            uploadFileResult.setFileSize(uploadFile.getTotalSize());
            uploadFileResult.setStorageType(StorageTypeEnums.LOCAL);

            if (uploadFile.getTotalChunks() == 1) {
                uploadFileResult.setFileSize(ossMultipartFile.getSize());
            }
            uploadFileResult.setIdentifier(uploadFile.getIdentifier());
            if (isComplete) {
                boolean renameTo = tempFile.renameTo(file);
                if(!renameTo){
                    logger.error("本地文件上传 变更文件名出错！");
                }
                FILE_URL_MAP.remove(uploadFile.getIdentifier());

                if (OssUtils.isImageFile(uploadFileResult.getExtendName())) {

                    InputStream is = null;
                    try {
                        is = new FileInputStream(OssUtils.getLocalSaveFile(fileUrl));

                        BufferedImage src = ImageIO.read(is);
                        uploadFileResult.setBufferedImage(src);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        IOUtils.closeQuietly(is);
                    }
                }

                uploadFileResult.setStatus(UploadFileStatusEnums.SUCCESS);
            } else {
                uploadFileResult.setStatus(UploadFileStatusEnums.UNCOMPLATE);
            }
        } catch (IOException e) {
            throw new UploadException(e);
        }

        return uploadFileResult;
    }

    @Override
    public void cancelUpload(UploadFile uploadFile) {
        String fileUrl = FILE_URL_MAP.get(uploadFile.getIdentifier());
        String tempFileUrl = fileUrl + "_tmp";
        String confFileUrl = fileUrl.replace("." + FilenameUtils.getExtension(fileUrl), ".conf");
        File tempFile = new File(tempFileUrl);
        if (tempFile.exists()) {
            tempFile.delete();
        }
        File confFile = new File(confFileUrl);
        if (confFile.exists()) {
            confFile.delete();
        }
    }

    @Override
    protected void doUploadFileChunk(OssMultipartFile ossMultipartFile, UploadFile uploadFile) {

    }

    @Override
    protected UploadFileResult organizationalResults(OssMultipartFile ossMultipartFile, UploadFile uploadFile) {
        return null;
    }

}
