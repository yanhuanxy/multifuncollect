package com.yanhuanxy.multifunexport.fileservice.factory;

import com.yanhuanxy.multifunexport.fileservice.dto.enums.FileOperationType;
import com.yanhuanxy.multifunexport.fileservice.dto.enums.StorageTypeEnums;
import com.yanhuanxy.multifunexport.fileservice.operation.BeanCreateStorageUtil;
import com.yanhuanxy.multifunexport.fileservice.operation.copy.Copier;
import com.yanhuanxy.multifunexport.fileservice.operation.delete.Deleter;
import com.yanhuanxy.multifunexport.fileservice.operation.download.Downloader;
import com.yanhuanxy.multifunexport.fileservice.operation.read.Reader;
import com.yanhuanxy.multifunexport.fileservice.operation.upload.Uploader;
import com.yanhuanxy.multifunexport.fileservice.operation.write.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author yym
 * @version 1.0
 */
public class OssFactory {
    private static final Logger logger = LoggerFactory.getLogger(OssFactory.class);

//    @Resource
//    private FastDFSCopier fastDFSCopier;
//
//    @Resource
//    private FastDFSUploader fastDFSUploader;
//
//    @Resource
//    private FastDFSDownloader fastDFSDownloader;
//
//    @Resource
//    private FastDFSDeleter fastDFSDeleter;
//
//    @Resource
//    private FastDFSReader fastDFSReader;
//
//    @Resource
//    private FastDFSPreviewer fastDFSPreviewer;
//
//    @Resource
//    private FastDFSWriter fastDFSWriter;

    @Autowired
    private ApplicationContext context;

    public OssFactory() {
        super();
    }

    /**
     * 上传
     * @param storageTypeEnums 策略 本地 、Minio
     */
    public Uploader getUploader(StorageTypeEnums storageTypeEnums) {
        BeanCreateStorageUtil<Uploader> uploadercreateBeanByStorageType = new BeanCreateStorageUtil<>(context);
        return uploadercreateBeanByStorageType.initCreateBeanByStorageType(storageTypeEnums, FileOperationType.UPLOAD);
    }

    /**
     * 上传
     * @param storageTypeEnums 策略 本地 、Minio
     */
    public Downloader getDownloader(StorageTypeEnums storageTypeEnums) {
        BeanCreateStorageUtil<Downloader> downloadercreateBeanByStorageType = new BeanCreateStorageUtil<>(context);
        return downloadercreateBeanByStorageType.initCreateBeanByStorageType(storageTypeEnums, FileOperationType.DOWNLOAD);
    }

    /**
     * 上传
     * @param storageTypeEnums 策略 本地 、Minio
     */
    public Deleter getDeleter(StorageTypeEnums storageTypeEnums) {
        BeanCreateStorageUtil<Deleter> deletercreateBeanByStorageType = new BeanCreateStorageUtil<>(context);
        return deletercreateBeanByStorageType.initCreateBeanByStorageType(storageTypeEnums, FileOperationType.DELETE);
    }

    /**
     * 读取
     * @param storageTypeEnums 策略 本地 、Minio
     */
    public Reader getReader(StorageTypeEnums storageTypeEnums) {
        BeanCreateStorageUtil<Reader> readercreateBeanByStorageType = new BeanCreateStorageUtil<>(context);
        return readercreateBeanByStorageType.initCreateBeanByStorageType(storageTypeEnums, FileOperationType.READ);
    }

    /**
     * 写入
     * @param storageTypeEnums 策略 本地 、Minio
     */
    public Writer getWriter(StorageTypeEnums storageTypeEnums) {
        BeanCreateStorageUtil<Writer> writercreateBeanByStorageType = new BeanCreateStorageUtil<>(context);
        return writercreateBeanByStorageType.initCreateBeanByStorageType(storageTypeEnums, FileOperationType.WRITE);
    }

//    /**
//     * 预览
//     * @param storageTypeEnums 策略 本地 、Minio
//     */
//    public Previewer getPreviewer(StorageTypeEnums storageTypeEnums) {
//        BeanCreateStorageUtil<Previewer> uploadercreateBeanByStorageType = new BeanCreateStorageUtil<>(context);
//        return uploadercreateBeanByStorageType.initCreateBeanByStorageType(storageTypeEnums, FileOperationType.PREVIEW);
//        Previewer previewer = null;
//        else if (StorageTypeEnums.FAST_DFS.getCode() == storageType) {
//            previewer = fastDFSPreviewer;
//        }
//        if (Objects.equals(StorageTypeEnums.MINIO, storageTypeEnums)) {
//            previewer = new MinioPreviewer(minioConfig, thumbImage);
//        }else{
//            previewer = new LocalStoragePreviewer(thumbImage);
//        }
//        return previewer;
//    }

    /**
     * 拷贝
     * @param storageTypeEnums 策略 本地 、Minio
     */
    public Copier getCopier(StorageTypeEnums storageTypeEnums) {
        BeanCreateStorageUtil<Copier> copiercreateBeanByStorageType = new BeanCreateStorageUtil<>(context);
        return copiercreateBeanByStorageType.initCreateBeanByStorageType(storageTypeEnums, FileOperationType.COPY);
    }

}
