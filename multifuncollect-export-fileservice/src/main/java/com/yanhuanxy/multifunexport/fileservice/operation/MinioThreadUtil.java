package com.yanhuanxy.multifunexport.fileservice.operation;

import com.yanhuanxy.multifunexport.fileservice.config.MinioConfig;
import com.yanhuanxy.multifunexport.tools.exception.ToolsException;
import com.yanhuanxy.multifunexport.tools.util.ThreadPoolUtils;
import io.minio.MinioClient;
import io.minio.messages.Item;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yym
 * @version 1.0
 */
public class MinioThreadUtil {
    private static final Logger logger = LoggerFactory.getLogger(MinioThreadUtil.class);

    private final ThreadPoolExecutor threadPool = ThreadPoolUtils.getThreadPool();

    /**
     * 文件下载
     * @param minioClientUtil
     * @param minio
     * @param minioSourceUrl
     * @param localTargetUrl
     * @param onlyFile
     * @throws Exception
     */
    public void batchDownload(MinioClientUtil minioClientUtil, MinioConfig minio, String minioSourceUrl, String localTargetUrl, boolean onlyFile) throws Exception {

        List<Item> minioFileList = minioClientUtil.getAllObjectsByPrefix(minio.getBucketName(), minioSourceUrl, onlyFile);
        int total = minioFileList.size();
        logger.info("总文件数： {}", total);
        if(total > 3){
            int pageSize = (total + 3 -1) / 3;
            for (int i = 0; i < 3; i++) {
                int startIndex = i * pageSize;
                int endIndex = startIndex + pageSize;
                if(endIndex > total){
                    endIndex = total;
                }
                List<Item> tmpMinioFileList = minioFileList.subList(startIndex, endIndex);
                prepareDownloadThread(tmpMinioFileList, minioClientUtil, minio, localTargetUrl);
            }
        }else {
            prepareDownloadThread(minioFileList, minioClientUtil, minio, localTargetUrl);
        }
    }

    private void prepareDownloadThread(List<Item> tmpMinioFileList, MinioClientUtil minioClientUtil, MinioConfig minio, String localTargetUrl){
        threadPool.execute(()->{
            try {
                doDownload(tmpMinioFileList, minioClientUtil, minio, localTargetUrl);
            } catch (Exception e) {
                logger.info("线程 {} 下载文件出错！文件数：{}", Thread.currentThread().getName(), tmpMinioFileList.size());
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    private void doDownload(List<Item> tmpMinioFileList, MinioClientUtil minioClientUtil, MinioConfig minio, String localTargetUrl) throws Exception {
        for (int i = 0; i < tmpMinioFileList.size(); i++) {
            Item item = tmpMinioFileList.get(i);
            if (item.isDir()) {
                // 迭代下载
                logger.info("-------下载文件-------- {} 文件夹：{}", Thread.currentThread().getName(),  item.objectName());
                batchDownload(minioClientUtil, minio, item.objectName(),localTargetUrl, Boolean.FALSE);
            }

            String objectName = item.objectName();
            // Create a local file with the same name as the object
            File file = new File(localTargetUrl.concat(File.separator).concat(objectName));
            // Create parent directories if needed
            File parentFile = file.getParentFile();
            if(!parentFile.exists()){
                boolean mkdirs = parentFile.mkdirs();
                logger.info("创建父级文件夹！{}", mkdirs);
            }
            // Get the object as an input stream
            try (InputStream stream = minioClientUtil.getObject(minio.getBucketName(), objectName)) {
                // Copy the input stream to the file
                FileUtils.copyInputStreamToFile(stream, file);
            }
            logger.info("文件：{} 下载成功！线程: {} index: {}", item.objectName(), Thread.currentThread().getName(), i);
        }
    }

    /**
     * 文件上传
     * @param minioClientUtil
     * @param minio
     * @param localSourceUrl
     * @param minioTargetUrl
     * @throws Exception
     */
    public void batchUpload(MinioClientUtil minioClientUtil, MinioConfig minio, String localSourceUrl, String minioTargetUrl) throws Exception {
        File file = new File(localSourceUrl);
        File[] localFiles = file.listFiles();
        if(localFiles == null){
            throw new ToolsException("文件上传时！未读取到文件");
        }
        List<File> localFilesList = Arrays.asList(localFiles);
        int total = localFilesList.size();
        logger.info("总文件数： {}", total);
        if(total > 3){
            int pageSize = (total + 3 - 1)/3;
            for (int i = 0; i < 3; i++) {
                int startIndex = i * pageSize;
                int endIndex = startIndex + pageSize;
                if(endIndex > total){
                    endIndex = total;
                }
                List<File> tmpLocalFileList = localFilesList.subList(startIndex, endIndex);
                prepareUploadThread(tmpLocalFileList, minioClientUtil, minio, minioTargetUrl);
            }
        }else {
            prepareUploadThread(localFilesList, minioClientUtil, minio, minioTargetUrl);
        }
    }

    private void prepareUploadThread(List<File> tmpLocalFileList, MinioClientUtil minioClientUtil, MinioConfig minio, String minioTargetUrl){
        threadPool.execute(()->{
            try {
                doUpload(tmpLocalFileList, minioClientUtil, minio, minioTargetUrl);
            } catch (Exception e) {
                logger.info("线程 {} 上传文件出错！文件数：{}", Thread.currentThread().getName(), tmpLocalFileList.size());
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    private void doUpload(List<File> tmpLocalFileList, MinioClientUtil minioClientUtil, MinioConfig minio, String minioTargetUrl) throws Exception {
        for (int i = 0; i < tmpLocalFileList.size(); i++) {
            File tmpFile = tmpLocalFileList.get(i);
            if(tmpFile.isFile()){
                try (FileInputStream inputStream = new FileInputStream(tmpFile)) {
                    minioClientUtil.putObject(minio.getBucketName(), (minioTargetUrl.concat("/").concat(tmpFile.getName())), inputStream, inputStream.available(), "application/octet-stream");
                }
                logger.info("文件：{} 上传成功！线程: {} index: {}", tmpFile.getName(), Thread.currentThread().getName(), i);
            }else if(tmpFile.isDirectory()){
                // 迭代下载
                logger.info("-------上传文件-------- {} 文件夹：{}", Thread.currentThread().getName(),  tmpFile.getPath());
                batchUpload(minioClientUtil, minio, tmpFile.getPath(), minioTargetUrl.concat("/").concat(tmpFile.getName()));
            }
        }
    }

    public static void main(String[] args) {
        MinioConfig minio = new MinioConfig();

        try {
            MinioClient minioClient = MinioClient.builder().endpoint(minio.getEndpoint()).credentials(minio.getAccessKey(), minio.getSecretKey()).build();
            MinioClientUtil minioClientUtil = new MinioClientUtil(minioClient);
            // 上传
            String localSourceUrl = "D:\\IDEAProjectGitlibWork\\asset\\asset-web\\target\\test-classes\\asset";
            String minioTargetUrl = "asset1";
            MinioThreadUtil minioThreadUtil = new MinioThreadUtil();
            minioThreadUtil.batchUpload(minioClientUtil, minio, localSourceUrl, minioTargetUrl);

            while (!minioThreadUtil.threadPool.isTerminated()){
                long taskCount = minioThreadUtil.threadPool.getTaskCount();
                long completedTaskCount = minioThreadUtil.threadPool.getCompletedTaskCount();
                logger.info("任务总数:" + taskCount + "； 已经完成任务数:" + completedTaskCount);
                if(taskCount == completedTaskCount){
                    break;
                }
            }
            // 下载
            String minioSourceUrl = "asset1/";
            String localTargetUrl = "D:\\IDEAProjectGitlibWork\\asset\\asset-web\\target\\test-classes\\";
            minioThreadUtil.batchDownload(minioClientUtil, minio, minioSourceUrl, localTargetUrl, Boolean.TRUE);

            while (!minioThreadUtil.threadPool.isTerminated()){
                long taskCount = minioThreadUtil.threadPool.getTaskCount();
                long completedTaskCount = minioThreadUtil.threadPool.getCompletedTaskCount();
                logger.info("任务总数:" + taskCount + "； 已经完成任务数:" + completedTaskCount);
                if(taskCount == completedTaskCount){
                    minioThreadUtil.threadPool.shutdownNow();
                    logger.info("停止中。。。");
                }else {
                    logger.info("还没停止。。。");
                }
                Thread.sleep(5000);
            }
            logger.info("执行关闭完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
