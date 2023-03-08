package com.yanhuanxy.multifunexport.fileservice.operation.delete.product;//package com.kelven.oss.export.operation.delete.product;
//
//import com.github.tobato.fastdfs.exception.FdfsServerException;
//import com.github.tobato.fastdfs.service.FastFileStorageClient;
//import com.kelven.oss.export.dto.operation.DeleteFile;
//import com.kelven.oss.export.operation.delete.Deleter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class FastDFSDeleter extends Deleter {
//
//    @Autowired
//    private FastFileStorageClient fastFileStorageClient;
//
//    @Override
//    public void delete(DeleteFile deleteFile) {
//        try {
//            fastFileStorageClient.deleteFile(deleteFile.getFileUrl().replace("M00", "group1"));
//        } catch (FdfsServerException e) {
//            logger.error("FDS文件删除异常", e);
//        }
//        deleteCacheFile(deleteFile);
//    }
//}
