package com.yanhuanxy.multifunexport.fileservice.operation.write.product;

import com.yanhuanxy.multifunexport.fileservice.dto.operation.WriteFile;
import com.yanhuanxy.multifunexport.fileservice.exception.operation.WriteException;
import com.yanhuanxy.multifunexport.fileservice.operation.write.Writer;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LocalStorageWriter extends Writer {

    @Override
    public void write(InputStream inputStream, WriteFile writeFile) {
        try (FileOutputStream out = new FileOutputStream(OssUtils.getStaticPath() + writeFile.getFileUrl())){
            int read;
            final byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            throw new WriteException("待写入的文件不存在:{}", e);
        } catch (IOException e) {
            throw new WriteException("IO异常:{}", e);
        }
    }
}
