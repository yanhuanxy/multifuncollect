package com.yanhuanxy.multifunexport.fileservice.operation.upload.product;

import com.yanhuanxy.multifunexport.fileservice.autoconfig.OssProperties;
import com.yanhuanxy.multifunexport.fileservice.config.MinioConfig;
import com.yanhuanxy.multifunexport.fileservice.dto.enums.StorageTypeEnums;
import com.yanhuanxy.multifunexport.fileservice.dto.enums.UploadFileStatusEnums;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.UploadFile;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.UploadFileResult;
import com.yanhuanxy.multifunexport.fileservice.exception.operation.UploadException;
import com.yanhuanxy.multifunexport.fileservice.operation.upload.Uploader;
import com.yanhuanxy.multifunexport.fileservice.operation.upload.request.OssMultipartFile;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MinioUploader extends Uploader implements InitializingBean {

    private MinioConfig minioConfig;

    @Resource
    private MinioClient minioClient;

    @Resource
    private OssProperties ossProperties;

    public MinioUploader(){
        super();
    }
    public MinioUploader(MinioConfig minioConfig){
        this.minioConfig = minioConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(ossProperties,"配置文件不存在");
        this.minioConfig = ossProperties.getMinio();
    }

    @Override
    public void cancelUpload(UploadFile uploadFile) {

    }

    @Override
    protected void doUploadFileChunk(OssMultipartFile ossMultipartFile, UploadFile uploadFile) {

    }

    @Override
    protected UploadFileResult organizationalResults(OssMultipartFile ossMultipartFile, UploadFile uploadFile) {
        return null;
    }

    protected UploadFileResult doUploadFlow(OssMultipartFile ossMultipartFile, UploadFile uploadFile) {
        UploadFileResult uploadFileResult = new UploadFileResult();
        try {
            String fileUrl = ossMultipartFile.getFileUrl(uploadFile.getUserCode(), uploadFile.getIdentifier());

            File tempFile =  OssUtils.getTempFile(fileUrl);
            File processFile = OssUtils.getProcessFile(fileUrl);

            byte[] fileData = ossMultipartFile.getUploadBytes();

            writeByteDataToFile(fileData, tempFile, uploadFile);

            //判断是否完成文件的传输并进行校验与重命名
            boolean isComplete = checkUploadStatus(uploadFile, processFile);
            uploadFileResult.setFileUrl(fileUrl);
            uploadFileResult.setFileName(ossMultipartFile.getFileName());
            uploadFileResult.setExtendName(ossMultipartFile.getExtendName());
            uploadFileResult.setFileSize(uploadFile.getTotalSize());
            uploadFileResult.setStorageType(StorageTypeEnums.MINIO);

            if (uploadFile.getTotalChunks() == 1) {
                uploadFile.setTotalSize(ossMultipartFile.getSize());
                uploadFileResult.setFileSize(ossMultipartFile.getSize());
            }
            uploadFileResult.setIdentifier(uploadFile.getIdentifier());
            if (isComplete) {

                minioUpload(fileUrl, tempFile, uploadFile);
                uploadFileResult.setFileUrl(fileUrl);
                boolean delete = tempFile.delete();
                if(!delete){
                    logger.warn("Minio上传后本地文件删除失败！");
                }

                if (OssUtils.isImageFile(uploadFileResult.getExtendName())) {
                    InputStream inputStream = null;
                    try {
                        inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(uploadFileResult.getFileUrl()).build());

                        BufferedImage src  = ImageIO.read(inputStream);
                        uploadFileResult.setBufferedImage(src);
                    } catch (IOException | InternalException | XmlParserException | InvalidResponseException | InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InsufficientDataException | ServerException e) {
                        logger.error("Minio 图片上传异常", e);
                    } finally {
                        IOUtils.closeQuietly(inputStream);
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

    private void minioUpload(String fileUrl, File file,  UploadFile uploadFile) {
        InputStream inputStream = null;
        try {
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
            if(!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
            }

            inputStream = new FileInputStream(file);
            minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileUrl).stream(
                                    inputStream, uploadFile.getTotalSize(), 1024 * 1024 * 5)
//                            .contentType("video/mp4")
                            .build());
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            logger.error("Minio文件上传异常！", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static void main(String[] args) {
        File files = new File("D:\\asset\\asset_8ef16775e04ba436594b9325bfae5ca0");
        List<File> filePaths =Arrays.stream(files.listFiles()).sorted(Comparator.comparing((b) -> Integer.parseInt(b.getName()))).toList();
        mergeFiles(filePaths, "合并后的文件2.zip");
    }

    public static void mergeFiles(List<File> filePaths, String outputFilePath) {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(new File(outputFilePath));
            for (File filePath : filePaths) {
                fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
