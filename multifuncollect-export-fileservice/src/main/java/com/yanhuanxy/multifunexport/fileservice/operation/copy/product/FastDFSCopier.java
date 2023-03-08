package com.yanhuanxy.multifunexport.fileservice.operation.copy.product;//package com.kelven.oss.export.operation.copy.product;
//
//import com.github.tobato.fastdfs.domain.StorePath;
//import com.github.tobato.fastdfs.service.AppendFileStorageClient;
//import com.kelven.oss.export.dto.operation.CopyFile;
//import com.kelven.oss.export.operation.copy.Copier;
//import org.apache.commons.compress.utils.IOUtils;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.io.InputStream;
//
//public class FastDFSCopier extends Copier {
//    @Resource
//    AppendFileStorageClient defaultAppendFileStorageClient;
//
//    @Override
//    public String copy(InputStream inputStream, CopyFile copyFile) {
//        StorePath storePath = new StorePath();
//        try {
//            storePath = defaultAppendFileStorageClient.uploadAppenderFile("group1", inputStream,
//                    inputStream.available(), copyFile.getExtendName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            IOUtils.closeQuietly(inputStream);
//        }
//        return storePath.getPath();
//    }
//}
