package com.yanhuanxy.multifunexport.fileservice.operation.download.product;//package com.kelven.oss.export.operation.download.product;
//
//import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
//import com.github.tobato.fastdfs.service.FastFileStorageClient;
//import com.kelven.oss.export.dto.operation.DownloadFile;
//import com.kelven.oss.export.operation.download.Downloader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//
//@Component
//public class FastDFSDownloader extends Downloader {
//    @Autowired
//    private FastFileStorageClient fastFileStorageClient;
//
//    @Override
//    public InputStream getInputStream(DownloadFile downloadFile) {
//        String group;
//        group = "group1";
//        String path = downloadFile.getFileUrl().substring(downloadFile.getFileUrl().indexOf("/") + 1);
//        DownloadByteArray downloadByteArray = new DownloadByteArray();
//        byte[] bytes;
//        if (downloadFile.getRange() != null) {
//            bytes = fastFileStorageClient.downloadFile(group, path,
//                    downloadFile.getRange().getStart(),
//                    downloadFile.getRange().getLength(),
//                    downloadByteArray);
//        } else {
//            bytes = fastFileStorageClient.downloadFile(group, path, downloadByteArray);
//        }
//        InputStream inputStream = new ByteArrayInputStream(bytes);
//        return inputStream;
//    }
//}
