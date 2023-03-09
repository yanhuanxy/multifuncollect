package com.yanhuanxy.multifunexport.fileservice.autoconfig;

import com.yanhuanxy.multifunexport.fileservice.factory.OssFactory;
import com.yanhuanxy.multifunexport.fileservice.operation.InitMinioClient;
import com.yanhuanxy.multifunexport.fileservice.operation.MinioClientUtil;
import com.yanhuanxy.multifunexport.fileservice.util.OssRedisLock;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import com.yanhuanxy.multifunexport.tools.redis.RedisUtil;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

import java.util.Objects;


@Configuration
@EnableConfigurationProperties({OssProperties.class})
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class OssAutoConfiguration {

    @Autowired
    private OssProperties ossProperties;

    @Bean
    public MinioClient InitMinioClient(){
        return new InitMinioClient(ossProperties.getMinio()).getMinioClient();
    }

    @Bean
    public MinioClientUtil createMinioClientUtil(MinioClient client){

        return new MinioClientUtil(client);
    }

    @Bean
    public OssFactory ossFactory() {
        OssUtils.LOCAL_STORAGE_PATH = ossProperties.getLocalStoragePath();
        String bucketName = ossProperties.getBucketName();
        if (Objects.isNull(bucketName)) {
            OssUtils.ROOT_PATH = "upload";
        } else {
            OssUtils.ROOT_PATH = ossProperties.getBucketName();
        }
        return new OssFactory();
    }

//    @Bean
//    public FastDFSCopier fastDFSCreater() {
//        return new FastDFSCopier();
//    }
//
//    @Bean
//    public FastDFSUploader fastDFSUploader() {
//        return new FastDFSUploader();
//    }
//
//    @Bean
//    public FastDFSDownloader fastDFSDownloader() {
//        return new FastDFSDownloader();
//    }
//
//    @Bean
//    public FastDFSDeleter fastDFSDeleter() {
//        return new FastDFSDeleter();
//    }
//
//    @Bean
//    public FastDFSReader fastDFSReader() {
//        return new FastDFSReader();
//    }
//
//    @Bean
//    public FastDFSWriter fastDFSWriter() {
//        return new FastDFSWriter();
//    }
//
//    @Bean
//    public FastDFSPreviewer fastDFSPreviewer() {
//        return new FastDFSPreviewer(ossProperties.getThumbImage());
//    }

    @Bean
    public OssRedisLock redisLock() {
        return new OssRedisLock();
    }

    @Bean
    public RedisUtil redisUtil() {
        return new RedisUtil();
    }

}
