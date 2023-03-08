package com.yanhuanxy.multifunexport.fileservice.operation.read.product;//package com.kelven.oss.export.operation.read.product;
//
//import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
//import com.github.tobato.fastdfs.service.FastFileStorageClient;
//import com.kelven.oss.export.dto.operation.ReadFile;
//import com.kelven.oss.export.exception.operation.ReadException;
//import com.kelven.oss.export.operation.read.Reader;
//import com.kelven.oss.export.util.ReadFileUtils;
//import org.apache.commons.io.FilenameUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//@Component
//public class FastDFSReader extends Reader {
//    @Autowired
//    private FastFileStorageClient fastFileStorageClient;
//    @Override
//    public String read(ReadFile readFile) {
//
//        String fileUrl = readFile.getFileUrl();
//        String fileType = FilenameUtils.getExtension(fileUrl);
//        try {
//            return ReadFileUtils.getContentByInputStream(fileType, getInputStream(readFile.getFileUrl()));
//        } catch (IOException e) {
//            throw new ReadException("读取文件失败", e);
//        }
//    }
//
//    public InputStream getInputStream(String fileUrl) {
//        String group;
//        group = "group1";
//        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
//        DownloadByteArray downloadByteArray = new DownloadByteArray();
//        byte[] bytes = fastFileStorageClient.downloadFile(group, path, downloadByteArray);
//        InputStream inputStream = new ByteArrayInputStream(bytes);
//        return inputStream;
//    }
//}
