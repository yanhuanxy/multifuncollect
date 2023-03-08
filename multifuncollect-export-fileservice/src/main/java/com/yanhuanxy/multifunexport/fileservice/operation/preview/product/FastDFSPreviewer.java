package com.yanhuanxy.multifunexport.fileservice.operation.preview.product;//package com.kelven.oss.export.operation.preview.product;
//
//import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
//import com.github.tobato.fastdfs.service.FastFileStorageClient;
//import com.kelven.oss.export.dto.ThumbImage;
//import com.kelven.oss.export.dto.operation.PreviewFile;
//import com.kelven.oss.export.operation.preview.Previewer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//
//@Component
//public class FastDFSPreviewer extends Previewer {
//
//    @Autowired
//    private FastFileStorageClient fastFileStorageClient;
//
//    public FastDFSPreviewer(){}
//
//    public FastDFSPreviewer(ThumbImage thumbImage) {
//
//        setThumbImage(thumbImage);
//    }
//
//    protected InputStream getInputStream(PreviewFile previewFile) {
//        String group = "group1";
//        String path = previewFile.getFileUrl().substring(previewFile.getFileUrl().indexOf("/") + 1);
//        DownloadByteArray downloadByteArray = new DownloadByteArray();
//        byte[] bytes = fastFileStorageClient.downloadFile(group, path, downloadByteArray);
//        InputStream inputStream = new ByteArrayInputStream(bytes);
//        return inputStream;
//    }
//
//}
