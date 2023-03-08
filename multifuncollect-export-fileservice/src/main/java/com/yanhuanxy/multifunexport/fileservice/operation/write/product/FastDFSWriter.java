package com.yanhuanxy.multifunexport.fileservice.operation.write.product;//package com.kelven.oss.export.operation.write.product;
//
//import com.github.tobato.fastdfs.service.AppendFileStorageClient;
//import com.kelven.oss.export.dto.operation.WriteFile;
//import com.kelven.oss.export.operation.write.Writer;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.InputStream;
//
//@Component
//public class FastDFSWriter extends Writer {
//
//    @Resource
//    AppendFileStorageClient defaultAppendFileStorageClient;
//    @Override
//    public void write(InputStream inputStream, WriteFile writeFile) {
//        defaultAppendFileStorageClient.modifyFile("group1", writeFile.getFileUrl(), inputStream,
//                writeFile.getFileSize(), 0);
//    }
//}
