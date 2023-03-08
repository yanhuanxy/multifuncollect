package com.yanhuanxy.multifunexport.fileservice.operation.preview.product;

import com.yanhuanxy.multifunexport.fileservice.dto.ThumbImage;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.PreviewFile;
import com.yanhuanxy.multifunexport.fileservice.operation.preview.Previewer;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class LocalStoragePreviewer extends Previewer {

    public LocalStoragePreviewer(){
        super();
    }
    public LocalStoragePreviewer(ThumbImage thumbImage) {
        setThumbImage(thumbImage);
    }

    @Override
    protected InputStream getInputStream(PreviewFile previewFile) {
        //设置文件路径
        File file = OssUtils.getLocalSaveFile(previewFile.getFileUrl());
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("文件预览异常", e);
        }

        return inputStream;

    }

}
